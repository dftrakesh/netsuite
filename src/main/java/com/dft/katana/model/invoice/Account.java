package com.dft.katana.model.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Account {

    private AccountContextSearch accountContextSearch;
    private String accountSearchDisplayName;
    private String accountSearchDisplayNameCopy;
    private String acctName;
    private String acctNumber;
    private ReferenceName acctType;
    private Double availableBalance;
    private Boolean bach;
    private Double balance;
    private Account billableExpensesAcct;
    private ReferenceName cashFlowRate;
    private NsResource category1099Misc;

    @JsonProperty("class")
    private Classification klass;

    private Integer curDocNum;
    private Currency currency;
    private DeferralAcct deferralAcct;
    private Department department;
    private String description;
    private String displayNameWithHierarchy;
    private Boolean eliminate;
    private String externalId;
    private String fullName;
    private ReferenceName generalRate;
    private String id;
    private Boolean includeChildren;
    private Boolean inventory;
    private Boolean isInactive;
    private Boolean issummary;
    private String lastModifiedDate;
    private String legalName;
    private List<Link> links;
    private Localizations localizations;
    private Location locationl;
    private Double mmaxamtpertran;
    private Double openingBalance;
    private Parent parent;
    private Boolean reconcilewithmatching;
    private String refName;
    private Restricttoaccountingbook restricttoaccountingbook;
    private Boolean revalue;
    private String sBankCompanyId;
    private String sBankName;
    private String sBankRoutingNumber;
    private ReferenceName sSpecAcct;
    private String sachmsg;
    private Subsidiary subsidiary;
    private String tranDate;
    private String unit;
    private UnitsType unitsType;
}