package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryDetail {

    private InventoryAssignment inventoryassignment;
    private IdFiled item;
    private IdFiled location;
    private Integer quantity;
}