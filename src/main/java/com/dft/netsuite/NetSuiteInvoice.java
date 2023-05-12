package com.dft.netsuite;

import com.dft.netsuite.model.credentials.Credentials;
import com.dft.netsuite.model.invoice.InvoiceRequest;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;

import static com.dft.netsuite.constantcodes.ConstantCode.INVOICE_ENDPOINT;

public class NetSuiteInvoice extends NetSuiteRestSdk {

    public NetSuiteInvoice(Credentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public Integer createInvoice(InvoiceRequest invoiceRequest) {
        URI uri = baseUrl(INVOICE_ENDPOINT);
        HttpRequest request = post(uri, invoiceRequest);
        return getRequestWrappedV2(request);
    }
}