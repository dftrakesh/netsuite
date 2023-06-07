package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxDetails {

    private List<Tax> items;
}