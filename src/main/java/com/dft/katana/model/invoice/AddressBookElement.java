package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class AddressBookElement {

    private Address addressBookAddress;
    private String addressBookAddress_text;
    private String addressId;
    private Boolean defaultBilling;
    private Boolean defaultShipping;
    private String id;
    private Integer internalId;
    private Boolean isResidential;
    private String label;
    private List<Link> links;
    private String refName;
}
