package com.dft.netsuite.model.credentials;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessToken {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;
    private String idToken;
    private LocalDateTime expireAt;
}