package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tax {

    private IdFiled taxDetailsReference;
    private String taxType;
    private IdFiled taxCode;
    private Double taxBasis;
    private Double taxRate;
    private Double taxAmount;
}