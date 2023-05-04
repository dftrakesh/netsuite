package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class ClasstranslationElement {

    private String language;
    private List<Link> links;
    private ReferenceName locale;
    private String name;
    private String refName;
}