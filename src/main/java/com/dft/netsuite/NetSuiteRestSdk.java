package com.dft.netsuite;

import com.dft.netsuite.model.credentials.AccessCredentials;
import com.dft.netsuite.model.credentials.AuthCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.dft.netsuite.constantcodes.ConstantCode.AUTHORIZE_ENDPOINT;

public class NetSuiteRestSdk {

    protected final HttpClient client;
    protected final String netSuiteDomain;
    protected final ObjectMapper objectMapper;
    protected final AuthCredentials authCredentials;

    protected AccessCredentials accessCredentials;

    int MAX_ATTEMPTS = 50;
    int TIME_OUT_DURATION = 60000;

    @SneakyThrows
    public NetSuiteRestSdk(AuthCredentials authCredentials) {
        this.authCredentials = authCredentials;
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
        this.accessCredentials = null;
        this.netSuiteDomain = "https://" + this.authCredentials.getInstanceId() + ".app.netsuite.com";

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
    protected URI baseUrl(String path) {
        return new URI(this.netSuiteDomain + path);
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
        return this.netSuiteDomain + AUTHORIZE_ENDPOINT + "?"
            + "scope=" + authCredentials.getScope() + "&"
            + "redirect_uri=" + URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8) + "&"
            + "response_type=code&"
            + "client_id=" + authCredentials.getClientId() + "&"
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
