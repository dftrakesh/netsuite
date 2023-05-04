package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class AccountContextSearchElement {

    private Accountingcontext accountingcontext;
    private String acctname;
    private String acctnumber;
    private String legalName;
    private List<Link> links;
    private ReferenceName object;
    private String refName;
}