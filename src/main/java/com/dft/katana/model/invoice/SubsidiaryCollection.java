package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class SubsidiaryCollection {

    private Integer count;
    private Boolean hasMore;
    private List<Subsidiary> items;
    private List<Link> links;
    private Integer offset;
    private Integer totalResults;
}