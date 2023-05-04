package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class Currency {

    private Integer currencyPrecision;
    private String displaySymbol;
    private Double exchangeRate;
    private String externalId;
    private String formatSample;
    private ReferenceName fxRateUpdateTimezone;
    private String id;
    private Boolean includeInFxRateUpdates;
    private Boolean isAnchorCurrency;
    private Boolean isBaseCurrency;
    private Boolean isInactive;
    private String lastModifiedDate;
    private List<Link> links;
    private ReferenceName locale;
    private String name;
    private Boolean overrideCurrencyFormat;
    private String refName;
    private String symbol;
    private ReferenceName symbolPlacement;
}