package com.dft.netsuite;

import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.dft.netsuite.model.customerrefund.CustomerRefundRequest;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import lombok.SneakyThrows;

import static com.dft.netsuite.constants.ConstantCode.ACCEPT;
import static com.dft.netsuite.constants.ConstantCode.APPLICATION_JSON;
import static com.dft.netsuite.constants.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constants.ConstantCode.CUSTOMER_REFUND_ENDPOINT;

public class NetSuiteCustomerRefund extends NetSuiteRestSdk {

    public NetSuiteCustomerRefund(NetSuiteCredentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public com.dft.netsuite.model.commons.Response createCustomerRefund(CustomerRefundRequest customerRefundRequest) {
        OAuthRequest request = new OAuthRequest(Verb.POST, netSuiteDomain + CUSTOMER_REFUND_ENDPOINT);
        request.setRealm(credentials.getRealm());
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.addHeader(ACCEPT, APPLICATION_JSON);
        request.setPayload(getString(customerRefundRequest));

        Response res = tryResend(request, 1);
        return getResponse(res);
    }

    @SneakyThrows
    public Response tryResend(OAuthRequest request, int count) {

        Response response = signAndExecute(request);

        if (response.getCode() == 429 && count < MAX_ATTEMPTS) {
            Thread.sleep(TIME_OUT_DURATION);
            return tryResend(request, count + 1);
        }
        return response;
    }
}