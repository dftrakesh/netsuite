package com.dft.netsuite.model.credentials;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NetSuiteCredentials {

    private String instanceId;
    private String scope;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expireAt;
    private String realm;
    private String tokenId;
    private String tokenSecret;
    private String consumerKey;
    private String consumerSecret;
    private Boolean tokenBasedAuthentication;
}