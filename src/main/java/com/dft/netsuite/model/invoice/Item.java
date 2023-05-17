package com.dft.netsuite.model.invoice;

import lombok.Data;

@Data
public class Item {

    private InventoryDetail inventoryDetail;
    private IdFiled item;
    private Integer line;
    private IdFiled location;
    private String taxDetailsReference;
    private Integer quantity;
    private Double rate;
}