package com.dft.netsuite.model.invoice;

import lombok.Data;

@Data
public class InventoryDetail {

    private InventoryAssignment inventoryassignment;
    private IdFiled item;
    private IdFiled location;
    private Integer quantity;
}