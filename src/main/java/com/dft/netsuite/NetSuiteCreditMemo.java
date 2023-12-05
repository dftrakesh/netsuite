package com.dft.netsuite;

import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.dft.netsuite.model.creditmemo.CreditMemoRequest;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import lombok.SneakyThrows;

import static com.dft.netsuite.constants.ConstantCode.ACCEPT;
import static com.dft.netsuite.constants.ConstantCode.APPLICATION_JSON;
import static com.dft.netsuite.constants.ConstantCode.CONTENT_TYPE;
import static com.dft.netsuite.constants.ConstantCode.CREDIT_MEMO_ENDPOINT;

public class NetSuiteCreditMemo extends NetSuiteRestSdk {

    public NetSuiteCreditMemo(NetSuiteCredentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public com.dft.netsuite.model.commons.Response updateCreditMemo(CreditMemoRequest creditMemoRequest, Long creditMemoId) {
        allowMethods("PATCH");
        OAuthRequest request = new OAuthRequest(Verb.PATCH, netSuiteDomain + CREDIT_MEMO_ENDPOINT + "/" + creditMemoId);
        request.setRealm(credentials.getRealm());
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.addHeader(ACCEPT, APPLICATION_JSON);
        request.setPayload(getString(creditMemoRequest));

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