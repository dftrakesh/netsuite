package com.dft.netsuite.model.customerpayment;

import com.dft.netsuite.model.invoice.IdFiled;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerPaymentRequest {

    @JsonProperty("customForm")
    private IdFiled customForm;

    @JsonProperty("account")
    private IdFiled account;

    @JsonProperty("customer")
    private IdFiled customer;

    @JsonProperty("apply")
    private Apply apply;

    @JsonProperty("location")
    private IdFiled location;

    @JsonProperty("memo")
    private String memo;

    @JsonProperty("trandate")
    private String tranDate;
}