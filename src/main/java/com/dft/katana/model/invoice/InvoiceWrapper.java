package com.dft.katana.model.invoice;

import lombok.Data;

@Data
public class InvoiceWrapper {

    private InvoiceCollection invoiceCollection;
    private String nsError;
}
