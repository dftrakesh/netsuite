package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvoiceRequest {

    private IdFiled account;
    private IdFiled currency;
    private IdFiled entity;

    @JsonProperty("custbody_njt_ecommerce_id")
    private String ecommerceId;

    private String otherRefNum;
    private ItemRoot item;
    private IdFiled location;
    private PostingPeriod postingPeriod;
    private String salesEffectiveDate;
    private String shipDate;
    private IdFiled status;
    private IdFiled subsidiary;
    private Double subtotal;
    private Double total;
    private Double totalAfterTaxes;
    private Double totalCostEstimate;
    private String tranDate;
}