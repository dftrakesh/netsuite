package com.dft.netsuite.model.inventoryItem;

import lombok.Data;

import java.util.List;

@Data
public class InventoryItemRoot {

    private Integer count;
    private Boolean hasMore;
    private List<InventoryItem> items;
    private Integer offset;
    private Integer totalResults;
}