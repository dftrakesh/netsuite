package com.dft.katana;

import com.dft.katana.handler.JsonBodyHandler;
import com.dft.katana.model.credentials.AccessCredentials;
import com.dft.katana.model.credentials.AccessToken;
import com.dft.katana.model.credentials.RequestAccessToken;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

import static com.dft.katana.constantcodes.ConstantCode.AUTHORIZATION;
import static com.dft.katana.constantcodes.ConstantCode.CONTENT_TYPE;
import static com.dft.katana.constantcodes.ConstantCode.X_WWW_FORM_URLENCODED;

public class NetsuteToken extends NetsuiteSDK {

    public NetsuteToken(AccessCredentials accessCredentials) {
        super(accessCredentials);
    }

    @SneakyThrows
    public AccessToken createToken(RequestAccessToken requestAccessToken) {

        String credentialValue = accessCredentials.getClientId() + ":" + accessCredentials.getClientSecret();
        String base64Encoded = Base64.getEncoder().encodeToString(credentialValue.getBytes());

        HashMap<String, String> params = objectMapper.convertValue(requestAccessToken, HashMap.class);
        URI uri = addParameters(baseUrl("/services/rest/auth/oauth2/v1/token"), params);

        HttpRequest request = HttpRequest.newBuilder(uri)
            .header(AUTHORIZATION, "Basic " + base64Encoded)
            .header(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse.BodyHandler<AccessToken> handler = new JsonBodyHandler<>(AccessToken.class);
        AccessToken accessToken = getRequestWrapped(request, handler);

        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(accessToken.getExpiresIn());
        accessToken.setExpireAt(dateTime);
        return accessToken;
    }
}