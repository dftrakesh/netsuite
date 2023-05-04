package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class ClasstranslationCollection {

    private Integer count;
    private Boolean hasMore;
    private ClasstranslationElement items;
    private List<Link> links;
    private Integer offset;
    private Integer totalResults;

}