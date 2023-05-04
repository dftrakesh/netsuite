package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class Subsidiary {

    private ReferenceName country;
    private Currency currency;
    private String email;
    private String externalId;
    private String fax;
    private String fullName;
    private String id;
    private Integer internalId;
    private Boolean isElimination;
    private Boolean isInactive;
    private String lastModifiedDate;
    private String legalName;
    private List<Link> links;
    private Address address;
    private String name;
    private Subsidiary parent;
    private String refName;
    private Customer customer;
    private Vendor representingVendor;
    private ReturnAddress returnAddress;
    private ShippingAddress shippingAddress;
    private String state;
    private String tranInternalPrefix;
    private String url;

}