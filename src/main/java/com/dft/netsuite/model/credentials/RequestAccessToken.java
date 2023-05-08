package com.dft.netsuite.model.credentials;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestAccessToken {

    private String code;
    private String redirectUri;
    private String grantType;
    private String codeVerifier;
}