package com.dft.netsuite.model.customerpayment;

import com.dft.netsuite.model.invoice.IdFiled;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {

    private Boolean apply;
    private IdFiled doc;
    private String refNum;
    private String type;
}