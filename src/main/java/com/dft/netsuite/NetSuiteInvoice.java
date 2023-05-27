package com.dft.netsuite;

import com.dft.netsuite.model.invoice.Response;
import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.dft.netsuite.model.invoice.InvoiceRequest;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;

import static com.dft.netsuite.constants.ConstantCode.INVOICE_ENDPOINT;

public class NetSuiteInvoice extends NetSuiteRestSdk {

    public NetSuiteInvoice(NetSuiteCredentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public Response createInvoice(InvoiceRequest invoiceRequest) {
        URI uri = baseUrl(INVOICE_ENDPOINT);
        HttpRequest request = post(uri, invoiceRequest);
        return getRequestWrappedV2(request);
    }
}