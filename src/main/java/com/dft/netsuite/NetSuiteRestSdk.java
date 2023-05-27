package com.dft.netsuite;

import com.dft.netsuite.handler.JsonBodyHandler;
import com.dft.netsuite.handler.NSApi;
import com.dft.netsuite.model.credentials.AccessToken;
import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.dft.netsuite.model.invoice.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth10aService;
import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.dft.netsuite.constants.ConstantCode.ACCEPT;
import static com.dft.netsuite.constants.ConstantCode.APPLICATION_JSON;
import static com.dft.netsuite.constants.ConstantCode.AUTHORIZATION;
import static com.dft.netsuite.constants.ConstantCode.AUTHORIZE_ENDPOINT;
import static com.dft.netsuite.constants.ConstantCode.BEARER;
import static com.dft.netsuite.constants.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constants.ConstantCode.OAUTH_VERSION;
import static com.dft.netsuite.constants.ConstantCode.TOKEN_ENDPOINT;
import static com.dft.netsuite.constants.ConstantCode.X_WWW_FORM_URLENCODED;

public class NetSuiteRestSdk {

    protected final HttpClient client;
    protected final String netSuiteDomain;
    protected final ObjectMapper objectMapper;
    protected final NetSuiteCredentials credentials;
    private final OAuth10aService auth10aService;
    int MAX_ATTEMPTS = 50;
    int TIME_OUT_DURATION = 60000;

    @SneakyThrows
    public NetSuiteRestSdk(NetSuiteCredentials credentials) {
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
        this.credentials = credentials;
        this.netSuiteDomain = "https://" + this.credentials.getInstanceId() + ".suitetalk.api.netsuite.com";
        this.auth10aService = new ServiceBuilder(credentials.getConsumerKey()).apiSecret(credentials.getConsumerSecret())
                .build(new NSApi());
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
    protected String signAndExecute(OAuthRequest request) {
        auth10aService.signRequest(new OAuth1AccessToken(credentials.getTokenId(), credentials.getTokenSecret()), request);
        return auth10aService.execute(request).getBody();
    }
    @SneakyThrows
    public void refreshToken() {
        Map<Object, Object> data = new HashMap<>();
        data.put("refresh_token", credentials.getRefreshToken());
        data.put("grant_type", "refresh_token");

        getToken(data);
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
        if (accessToken.getError() != null) throw new Exception("AccessToken: " + accessToken.getError());
        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
        accessToken.setExpireAt(dateTime);

        String refreshToken = credentials.getRefreshToken();
        if (accessToken.getRefreshToken() != null) refreshToken = accessToken.getRefreshToken();

        credentials.setAccessToken(accessToken.getAccessToken());
        credentials.setRefreshToken(refreshToken);
        credentials.setExpireAt(accessToken.getExpireAt());

        return accessToken;
    }

    public void getAccessCredentials() {
        if (credentials.getExpireAt() != null && !LocalDateTime.now().isAfter(credentials.getExpireAt()))
            return;
        if (credentials.getRefreshToken() != null) refreshToken();
    }

    @SneakyThrows
    public HttpRequest get(URI uri) {
        getAccessCredentials();

        String authorizationHeader;
        if (credentials.getTokenBasedAuthentication()) {
           authorizationHeader = getAuthorizationHeader(uri);
        } else {
            authorizationHeader = BEARER + credentials.getAccessToken();
        }

        return HttpRequest.newBuilder(uri)
            .header(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
            .header(AUTHORIZATION, authorizationHeader)
            .header(ACCEPT, APPLICATION_JSON)
            .GET()
            .build();
    }

    @SneakyThrows
    public String getAuthorizationHeader(URI uri) {
        String encodedUrl = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8);
        String encodedHttpMethod = "GET";
        String consumerKey = credentials.getConsumerKey();
        String nonce = String.valueOf((int) (Math.random() * 100000000));
        String signatureMethod = "HMAC-SHA256";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String tokenKey = credentials.getTokenId();
        String consumerSecret = credentials.getConsumerSecret();
        String tokenSecret = credentials.getTokenSecret();

        Map<String, String> parameters = new TreeMap<>();
        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_nonce", nonce);
        parameters.put("oauth_signature_method", signatureMethod);
        parameters.put("oauth_timestamp", timestamp);
        parameters.put("oauth_token", tokenKey);
        parameters.put("oauth_version", OAUTH_VERSION);

        String baseString = generateSignatureBaseString(encodedHttpMethod, encodedUrl, parameters);
        String key = encodeKey(consumerSecret, tokenSecret);
        String signature = generateSignature(baseString, key);

        return "OAuth oauth_consumer_key=\"" +
                consumerKey +
                "\", oauth_token=\"" +
                tokenKey +
                "\", oauth_nonce=\"" +
                nonce +
                "\", oauth_timestamp=\"" +
                timestamp +
                "\", oauth_signature_method=\"" +
                signatureMethod +
                "\", oauth_version=\"" +
                OAUTH_VERSION +
                "\", realm=\"" +
                credentials.getRealm() +
                "\", oauth_signature=\"" +
                signature +
                "\"";
    }

    String generateSignatureBaseString(String encodedHttpMethod, String encodedUrl, Map<String, String> parameters) {
        TreeMap<String, String> sortedParameters = new TreeMap<>(parameters);
        StringBuilder parameterString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParameters.entrySet()) {
            if (parameterString.length() > 0) {
                parameterString.append('&');
            }
            parameterString.append(entry.getKey());
            parameterString.append('=');
            parameterString.append(entry.getValue());
        }

        return encodedHttpMethod.toUpperCase() +
                '&' +
                encodedUrl +
                '&' +
                URLEncoder.encode(parameterString.toString(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    String generateSignature(String baseString, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(Base64.getEncoder().encodeToString(signatureBytes), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    String encodeKey(String consumerSecret, String tokenSecret) {
        String encodedConsumerSecret = URLEncoder.encode(consumerSecret, StandardCharsets.UTF_8);
        String encodedTokenSecret = URLEncoder.encode(tokenSecret, StandardCharsets.UTF_8);
        return encodedConsumerSecret + "&" + encodedTokenSecret;
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
    protected HttpRequest patch(URI uri, String jsonBody) {
        getAccessCredentials();
        return HttpRequest.newBuilder(uri)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .header(AUTHORIZATION, BEARER + credentials.getAccessToken())
            .header(ACCEPT, APPLICATION_JSON)
            .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
    }

    @SneakyThrows
    protected HttpRequest patch(URI uri, Object body) {
        String jsonBody = objectMapper.writeValueAsString(body);
        return patch(uri, jsonBody);
    }

    protected URI baseUrl(String path) {
        return URI.create(netSuiteDomain + path);
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
    protected Response getRequestWrappedV2(HttpRequest request) {

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenComposeAsync(response -> tryResend(client, request, HttpResponse.BodyHandlers.ofString(), response, 1))
            .thenApplyAsync(stringHttpResponse -> {
                String body = stringHttpResponse.body();
                Response response = new Response();
                response.setStatus(stringHttpResponse.statusCode());

                if (body != null && !body.isEmpty()) {
                    response = convertBody(body, Response.class);
                }
                return response;
            })
            .get();
    }

    @SneakyThrows
    protected  <T> T convertBody(String body, Class<T> tClass) {
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

    @SneakyThrows
    protected String getString(Object o) {
        return objectMapper.writeValueAsString(o);
    }
}
