package com.dft.netsuite;

import com.dft.netsuite.handler.JsonBodyHandler;
import com.dft.netsuite.model.invoice.CreateInvoiceResponse;
import com.dft.netsuite.model.credentials.AccessToken;
import com.dft.netsuite.model.credentials.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.dft.netsuite.constantcodes.ConstantCode.ACCEPT;
import static com.dft.netsuite.constantcodes.ConstantCode.APPLICATION_JSON;
import static com.dft.netsuite.constantcodes.ConstantCode.AUTHORIZATION;
import static com.dft.netsuite.constantcodes.ConstantCode.AUTHORIZE_ENDPOINT;
import static com.dft.netsuite.constantcodes.ConstantCode.BEARER;
import static com.dft.netsuite.constantcodes.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constantcodes.ConstantCode.TOKEN_ENDPOINT;
import static com.dft.netsuite.constantcodes.ConstantCode.X_WWW_FORM_URLENCODED;

public class NetSuiteRestSdk {

    protected final HttpClient client;
    protected final String netSuiteDomain;
    protected final ObjectMapper objectMapper;
    protected final Credentials credentials;

    int MAX_ATTEMPTS = 50;
    int TIME_OUT_DURATION = 60000;

    @SneakyThrows
    public NetSuiteRestSdk(Credentials credentials) {
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
        this.credentials = credentials;
        this.netSuiteDomain = "https://" + this.credentials.getInstanceId() + ".suitetalk.api.netsuite.com";
    }

    @SneakyThrows
    public AccessToken createToken(String code, String redirectUrl) {
        Map<Object, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("redirect_uri", redirectUrl);
        data.put("grant_type", "authorization_code");

        return getToken(data);
    }

    @SneakyThrows
    public AccessToken refreshToken() {
        Map<Object, Object> data = new HashMap<>();
        data.put("refresh_token", credentials.getRefreshToken());
        data.put("grant_type", "refresh_token");

        return getToken(data);
    }

    @SneakyThrows
    public AccessToken getToken(Map<Object, Object> data) {
        String strBasicAuth = this.credentials.getClientId() + ":" + this.credentials.getClientSecret();
        String basicAuthBase64 = Base64.getEncoder().encodeToString(strBasicAuth.getBytes(StandardCharsets.UTF_8));
        URI uri = URI.create("https://" + this.credentials.getInstanceId() + ".app.netsuite.com" + TOKEN_ENDPOINT);

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
            .header(AUTHORIZATION, "Basic " + basicAuthBase64)
            .header(ACCEPT, APPLICATION_JSON)
            .POST(ofFormData(data))
            .build();

        HttpResponse.BodyHandler<AccessToken> handler = new JsonBodyHandler<>(AccessToken.class);
        AccessToken accessToken = getRequestWrapped(request, handler);
        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
        accessToken.setExpireAt(dateTime);

        String refreshToken = credentials.getRefreshToken();
        if (accessToken.getRefreshToken() != null) refreshToken = accessToken.getRefreshToken();

        credentials.setAccessToken(accessToken.getAccessToken());
        credentials.setRefreshToken(refreshToken);
        credentials.setExpireAt(accessToken.getExpireAt());

        return accessToken;
    }

    @SneakyThrows
    public Credentials getAccessCredentials() {
        if (credentials.getExpireAt() != null && !LocalDateTime.now().isAfter(credentials.getExpireAt()))
            return credentials;
        if (credentials.getRefreshToken() != null) refreshToken();
        return credentials;
    }

    @SneakyThrows
    public HttpRequest get(URI uri) {
        getAccessCredentials();
        return HttpRequest.newBuilder(uri)
            .header(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
            .header(AUTHORIZATION, BEARER + credentials.getAccessToken())
            .header(ACCEPT, APPLICATION_JSON)
            .GET()
            .build();
    }

    @SneakyThrows
    protected HttpRequest post(URI uri, String jsonBody) {
        getAccessCredentials();
        return HttpRequest.newBuilder(uri)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER + credentials.getAccessToken())
            .header(ACCEPT, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
    }

    @SneakyThrows
    protected HttpRequest post(URI uri, Object body) {
        String jsonBody = objectMapper.writeValueAsString(body);
        return post(uri, jsonBody);
    }

    @SneakyThrows
    protected URI baseUrl(String path) {
        return new URI(netSuiteDomain + path);
    }

    @SneakyThrows
    protected URI addParameters(URI uri, HashMap<String, String> params) {

        if (params == null) return uri;
        String query = uri.getQuery();
        StringBuilder builder = new StringBuilder();
        if (query != null)
            builder.append(query);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String keyValueParam = entry.getKey() + "=" + entry.getValue();
            if (!builder.toString().isEmpty())
                builder.append("&");
            builder.append(keyValueParam);
        }
        return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), builder.toString(), uri.getFragment());
    }

    @SneakyThrows
    public <T> T getRequestWrapped(HttpRequest request, HttpResponse.BodyHandler<T> handler) {

        return client
            .sendAsync(request, handler)
            .thenCompose(response -> tryResend(client, request, handler, response, 1))
            .get()
            .body();
    }

    @SneakyThrows
    protected CreateInvoiceResponse getRequestWrappedV2(HttpRequest request) {

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenComposeAsync(response -> tryResend(client, request, HttpResponse.BodyHandlers.ofString(), response, 1))
            .thenApplyAsync(stringHttpResponse -> {
                String body = stringHttpResponse.body();
                CreateInvoiceResponse createInvoiceResponse = new CreateInvoiceResponse();
                createInvoiceResponse.setStatus(stringHttpResponse.statusCode());

                if (body != null && !body.isEmpty()) {
                    createInvoiceResponse = convertBody(body, CreateInvoiceResponse.class);
                }
                return createInvoiceResponse;
            })
            .get();
    }

    @SneakyThrows
    private <T> T convertBody(String body, Class<T> tClass) {
        return objectMapper.readValue(body, tClass);
    }

    @SneakyThrows
    public <T> CompletableFuture<HttpResponse<T>> tryResend(HttpClient client,
                                                            HttpRequest request,
                                                            HttpResponse.BodyHandler<T> handler,
                                                            HttpResponse<T> resp, int count) {
        if (resp.statusCode() == 429 && count < MAX_ATTEMPTS) {
            Thread.sleep(TIME_OUT_DURATION);
            return client.sendAsync(request, handler)
                .thenComposeAsync(response -> tryResend(client, request, handler, response, count + 1));
        }
        return CompletableFuture.completedFuture(resp);
    }

    public String getAuthorizationUrl(String redirectUrl) {
        return "https://" + this.credentials.getInstanceId() + ".app.netsuite.com" + AUTHORIZE_ENDPOINT + "?"
            + "scope=" + credentials.getScope() + "&"
            + "redirect_uri=" + URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8) + "&"
            + "response_type=code&"
            + "client_id=" + credentials.getClientId() + "&"
            + "state=" + UUID.randomUUID().toString().replace("-", "");
    }

    public HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(entry.getKey().toString());
            builder.append("=");
            builder.append(entry.getValue().toString());
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
