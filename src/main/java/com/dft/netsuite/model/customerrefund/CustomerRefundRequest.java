package com.dft.netsuite.model.customerrefund;

import com.dft.netsuite.model.customerpayment.Apply;
import com.dft.netsuite.model.invoice.IdFiled;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerRefundRequest {

    private IdFiled customer;
    private IdFiled account;
    private String memo;
    private Apply apply;
    private IdFiled location;
    private IdFiled department;
    private String tranDate;
}