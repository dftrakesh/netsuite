package com.dft.netsuite.model.customerpayment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Apply {

    private List<Item> items;
}
