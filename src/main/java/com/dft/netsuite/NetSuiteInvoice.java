package com.dft.netsuite;

import com.dft.netsuite.model.credentials.Credentials;
import com.dft.netsuite.model.invoice.InvoiceRequest;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;

import static com.dft.netsuite.constantcodes.ConstantCode.INVOICE_ENDPOINT;

public class NetSuiteInvoice extends NetSuiteRestSdk {

    private final String domain;

    public NetSuiteInvoice(Credentials credentials) {
        super(credentials);
        this.domain = "https://" + this.credentials.getInstanceId() + ".suitetalk.api.netsuite.com";
    }

    @SneakyThrows
    public Integer createInvoice(InvoiceRequest invoiceRequest) {
        getAccessCredentials();
        URI uri = URI.create(domain + INVOICE_ENDPOINT);
        String jsonBody = objectMapper.writeValueAsString(invoiceRequest);
        HttpRequest request = post(uri, jsonBody);
        return getRequestWrappedV2(request);
    }
}