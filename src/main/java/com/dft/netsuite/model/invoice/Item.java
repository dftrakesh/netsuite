package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {

    private IdFiled department;
    private InventoryDetail inventoryDetail;
    private IdFiled item;
    private Integer line;
    private IdFiled location;
    private String taxDetailsReference;
    private Integer quantity;
    private Double rate;
}