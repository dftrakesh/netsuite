package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceCollection {

    private Integer count;
    private Boolean hasMore;
    private List<Invoice> items;
    private List<Link> links;
    private Integer offset;
    private Integer totalResults;
}