package com.dft.netsuite.model.credentials;

import lombok.Data;

@Data
public class AuthCredentials {

    private String instanceId;
    private String scope;
    private String clientId;
    private String clientSecret;
}