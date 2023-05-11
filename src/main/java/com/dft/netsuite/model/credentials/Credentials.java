package com.dft.netsuite.model.credentials;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Credentials {

    private String instanceId;
    private String scope;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expireAt;
}