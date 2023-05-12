package com.dft.netsuite.model.commen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorObject {

    private String type;
    private String title;
    private Integer status;

    @JsonProperty("o:errorDetails")
    private List<ErrorDetails> errorDetails;
}