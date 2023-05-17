package com.dft.netsuite.model.invoice;

import lombok.Data;

@Data
public class Tax {

    private IdFiled taxDetailsReference;
    private String taxType;
    private IdFiled taxCode;
    private Double taxBasis;
    private Double taxRate;
    private Double taxAmount;
}