package com.dft.netsuite;

import com.dft.netsuite.model.credentials.NetSuiteCredentials;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import lombok.SneakyThrows;

import java.util.Map;

public class Suiteql extends NetSuiteRestSdk {

    private static final String SUITEQL_ENDPOINT = "/services/rest/query/v1/suiteql";

    public Suiteql(NetSuiteCredentials netSuiteCredentials) {
        super(netSuiteCredentials);
    }

    @SneakyThrows
    public String executeQuery(String query) {
        OAuthRequest request = new OAuthRequest(Verb.POST, netSuiteDomain + SUITEQL_ENDPOINT);
        request.setRealm(credentials.getRealm());
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");
        request.addHeader("Prefer", "transient");
        request.setPayload(getString(Map.of("q", query)));

        return signAndExecute(request).getBody();
    }

    public <T> T executeQuery(String query, Class<T> tClass) {
        String response = executeQuery(query);
        return convertBody(response, tClass);
    }
}
