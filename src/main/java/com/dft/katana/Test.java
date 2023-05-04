package com.dft.katana;

import com.dft.katana.model.credentials.AccessCredentials;
import com.dft.katana.model.credentials.AccessToken;
import com.dft.katana.model.credentials.RequestAccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;

public class Test extends Thread {
    public static void main(String[] args) {
        AccessCredentials accessCredentials = AccessCredentials.builder()
            .clientSecret()
            .refreshToken()
            .accessToken()
            .accountId()
            .clientId()
            .expireAt()
            .build();

        NetsuteToken netsuiteSDK = new NetsuteToken(accessCredentials);

        RequestAccessToken requestAccessToken = new RequestAccessToken();
        requestAccessToken.setCode();
        requestAccessToken.setCodeVerifier();
        requestAccessToken.setGrantType();
        requestAccessToken.setRedirectUri();

        AccessToken token = netsuiteSDK.createToken(requestAccessToken);
        System.out.println("token: " + token);
    }

    @SneakyThrows
    public static String getJson(Object o) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}