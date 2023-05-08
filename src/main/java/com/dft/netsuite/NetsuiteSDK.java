package com.dft.netsuite;

import com.dft.netsuite.model.credentials.AccessCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.dft.netsuite.constantcodes.ConstantCode.BASE_ENDPOINT;
import static com.dft.netsuite.constantcodes.ConstantCode.HTTPS;

public class NetsuiteSDK {

    protected final AccessCredentials accessCredentials;
    protected final HttpClient client;
    protected final ObjectMapper objectMapper;
    int MAX_ATTEMPTS = 50;
    int TIME_OUT_DURATION = 60000;

    public NetsuiteSDK(AccessCredentials accessCredentials) {
        this.accessCredentials = accessCredentials;
        client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

//    @SneakyThrows
//    protected HttpRequest get(URI uri) {
//        return HttpRequest.newBuilder(uri)
//            .header(AUTHORIZATION, BEARER + accessCredentials)
//            .header(CONTENT_TYPE, "application/json")
//            .header(ACCEPT, "application/json")
//            .GET()
//            .build();
//    }
//
//
//    @SneakyThrows
//    protected HttpRequest post(URI uri, String jsonBody) {
//        return HttpRequest.newBuilder(uri)
//            .header(AUTHORIZATION, BEARER + accessCredentials)
//            .header(CONTENT_TYPE, "application/json")
//            .header(ACCEPT, "application/json")
//            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//            .build();
//    }

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
        return new URI(HTTPS + accessCredentials.getAccountId() + BASE_ENDPOINT + path);
    }

    @SneakyThrows
    public <T> T getRequestWrapped(HttpRequest request, HttpResponse.BodyHandler<T> handler) {

        return client
            .sendAsync(request, handler)
            .thenCompose(response -> {
                System.out.println(response.body());
                return tryResend(client, request, handler, response, 1);
            })
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


}
