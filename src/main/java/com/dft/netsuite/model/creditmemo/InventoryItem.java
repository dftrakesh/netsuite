package com.dft.netsuite.model.creditmemo;

import com.dft.netsuite.model.invoice.IdFiled;
import com.dft.netsuite.model.invoice.InventoryDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryItem {

    private Double amount;
    private InventoryDetail inventoryDetail;
    private IdFiled item;
    private Integer quantity;
    private Double rate;
}