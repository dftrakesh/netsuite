package com.dft.netsuite;

import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.dft.netsuite.model.customerpayment.CustomerPaymentRequest;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import lombok.SneakyThrows;

import static com.dft.netsuite.constants.ConstantCode.ACCEPT;
import static com.dft.netsuite.constants.ConstantCode.APPLICATION_JSON;
import static com.dft.netsuite.constants.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constants.ConstantCode.CUSTOMER_PAYMENT_ENDPOINT;

public class NetSuiteCustomerPayment extends NetSuiteRestSdk {

    public NetSuiteCustomerPayment(NetSuiteCredentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public com.dft.netsuite.model.commons.Response createCustomerPayment(CustomerPaymentRequest customerPaymentRequest) {
        OAuthRequest request = new OAuthRequest(Verb.POST, netSuiteDomain + CUSTOMER_PAYMENT_ENDPOINT);
        request.setRealm(credentials.getRealm());
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.addHeader(ACCEPT, APPLICATION_JSON);
        request.setPayload(getString(customerPaymentRequest));

        Response res = signAndExecute(request);
        return getResponse(res);
    }
}