package com.dft.netsuite.model.invoice;

import lombok.Data;

@Data
public class InventoryAssignmentItem {

    private IdFiled issueInventoryNumber;
    private Integer quantity;
}