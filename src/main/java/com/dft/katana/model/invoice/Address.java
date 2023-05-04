package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class Address {

    private String addr1;
    private String addr2;
    private String addr3;
    private String addrPhone;
    private String addrText;
    private String addressee;
    private String attention;
    private String city;
    private ReferenceName country;
    private String externalId;
    private String lastModifiedDate;
    private List<Link> links;
    private Boolean override;
    private String refName;
    private String state;
    private String zip;
}