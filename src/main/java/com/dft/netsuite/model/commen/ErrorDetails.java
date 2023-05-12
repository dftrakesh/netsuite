package com.dft.netsuite.model.commen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {

    private String detail;

    @JsonProperty("o:errorCode")
    private String errorCode;
}
