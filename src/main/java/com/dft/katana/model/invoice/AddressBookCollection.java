package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class AddressBookCollection {

    private Integer count;
    private Boolean hasMore;
    private List<AddressBookElement> items;
    private List<Link> links;
    private Integer offset;
    private Integer totalResults;
}