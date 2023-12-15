package com.dft.netsuite.model.creditmemo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditMemoItemRoot {

    private String memo;
    private List<InventoryItem> items;
}