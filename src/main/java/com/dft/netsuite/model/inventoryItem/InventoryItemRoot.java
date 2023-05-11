package com.dft.netsuite.model.inventoryItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryItemRoot {

    private Integer count;
    private Boolean hasMore;
    private List<InventoryItem> items;
    private Integer offset;
    private Integer totalResults;
}