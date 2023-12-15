package com.dft.netsuite.model.creditmemo;

import com.dft.netsuite.model.invoice.IdFiled;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditMemoRequest {

    private IdFiled department;
    private CreditMemoItemRoot item;
    private String memo;
}