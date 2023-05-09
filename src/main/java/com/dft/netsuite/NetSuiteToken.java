package com.dft.netsuite;

import com.dft.netsuite.handler.JsonBodyHandler;
import com.dft.netsuite.model.credentials.AccessCredentials;
import com.dft.netsuite.model.credentials.AccessToken;
import com.dft.netsuite.model.credentials.AuthCredentials;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.dft.netsuite.constantcodes.ConstantCode.TOKEN_ENDPOINT;

public class NetSuiteToken extends NetSuiteRestSdk {

    public NetSuiteToken(AuthCredentials authCredentials) {
        super(authCredentials);
    }

    @SneakyThrows
    public AccessToken createToken(String code, String redirectUrl) {
        Map<Object, Object> data = new HashMap<>();
        data.put("code", code);
        data.put("redirect_uri", redirectUrl);
        data.put("grant_type", "authorization_code");

        String strBasicAuth = this.authCredentials.getClientId() + ":" + this.authCredentials.getClientSecret();
        String basicAuthBase64 = Base64.getEncoder().encodeToString(strBasicAuth.getBytes(StandardCharsets.UTF_8));
        URI uri = URI.create(netSuiteDomain + TOKEN_ENDPOINT);

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + basicAuthBase64)
            .header("Accepts", "application/json")
            .POST(ofFormData(data))
            .build();

        HttpResponse.BodyHandler<AccessToken> handler = new JsonBodyHandler<>(AccessToken.class);
        AccessToken accessToken = getRequestWrapped(request, handler);
        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
        accessToken.setExpireAt(dateTime);
        return accessToken;
    }

    @SneakyThrows
    public AccessToken refreshToken() {
        Map<Object, Object> data = new HashMap<>();
        data.put("refresh_token", accessCredentials.getRefreshToken());
        data.put("grant_type", "refresh_token");

        String strBasicAuth = this.authCredentials.getClientId() + ":" + this.authCredentials.getClientSecret();
        String basicAuthBase64 = Base64.getEncoder().encodeToString(strBasicAuth.getBytes(StandardCharsets.UTF_8));
        URI uri = URI.create(netSuiteDomain + TOKEN_ENDPOINT);

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + basicAuthBase64)
            .header("Accepts", "application/json")
            .POST(ofFormData(data))
            .build();

        HttpResponse.BodyHandler<AccessToken> handler = new JsonBodyHandler<>(AccessToken.class);
        AccessToken accessToken = getRequestWrapped(request, handler);
        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
        accessToken.setExpireAt(dateTime);
        return accessToken;
    }


    @SneakyThrows
    public AccessCredentials getAccessCredentials() {
        if (this.accessCredentials != null && !LocalDateTime.now().isAfter(accessCredentials.getExpireAt())) {
            return accessCredentials;
        }

        AccessToken accessToken = refreshToken();
        accessCredentials.setAccessToken(accessToken.getAccessToken());
        accessCredentials.setExpireAt(LocalDateTime.now().plusSeconds(accessToken.getExpiresIn()));

        return accessCredentials;
    }
}