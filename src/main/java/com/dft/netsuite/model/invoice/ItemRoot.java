package com.dft.netsuite.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class ItemRoot {

    private List<Item> items;
}