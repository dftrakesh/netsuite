package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private String type;
    private String title;
    private Integer status;

    @JsonProperty("o:errorDetails")
    private List<ErrorDetails> errorDetails;
}