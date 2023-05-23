package com.dft.netsuite.model.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Credentials {

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