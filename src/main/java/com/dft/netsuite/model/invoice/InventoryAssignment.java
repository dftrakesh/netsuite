package com.dft.netsuite.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class InventoryAssignment {

    private List<InventoryAssignmentItem> items;
}