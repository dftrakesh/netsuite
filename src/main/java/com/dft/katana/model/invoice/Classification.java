package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class Classification {

    private ClasstranslationCollection classtranslation;
    private String externalId;
    private String fullName;
    private String id;
    private Boolean includeChildren;
    private Integer internalId;
    private Boolean isInactive;
    private String lastModifiedDate;
    private List<Link> links;
    private String name;
    private Classification parent;
    private String refName;
    private SubsidiaryCollection subsidiary;
}