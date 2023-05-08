package com.dft.netsuite;

import com.dft.netsuite.handler.JsonBodyHandler;
import com.dft.netsuite.model.credentials.AccessCredentials;
import com.dft.netsuite.model.credentials.RequestAccessToken;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;

import static com.dft.netsuite.constantcodes.ConstantCode.AUTHORIZATION;
import static com.dft.netsuite.constantcodes.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constantcodes.ConstantCode.X_WWW_FORM_URLENCODED;

public class NetsuteToken extends NetsuiteSDK {

    public NetsuteToken(AccessCredentials accessCredentials) {
        super(accessCredentials);
    }

    @SneakyThrows
    public String createToken(RequestAccessToken requestAccessToken) {
        String originalInput = accessCredentials.getClientId() + ":" + accessCredentials.getClientSecret();
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

        HashMap<String, String> params = objectMapper.convertValue(requestAccessToken, HashMap.class);
        URI uri = addParameters(baseUrl("/services/rest/auth/oauth2/v1/token"), params);
        System.out.println("params:" + params);

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header(AUTHORIZATION, "Basic " + encodedString)
            .header(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
            .POST(HttpRequest.BodyPublishers.ofString(""))
            .build();

        HttpResponse.BodyHandler<String> handler = new JsonBodyHandler<>(String.class);
        var accessToken = getRequestWrapped(request, handler);

//        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
//        accessToken.setExpireAt(dateTime);
        return accessToken;
    }
}