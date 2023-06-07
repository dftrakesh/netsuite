package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryAssignmentItem {

    private IdFiled issueInventoryNumber;
    private Integer quantity;
}