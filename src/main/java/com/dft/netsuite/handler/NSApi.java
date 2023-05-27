package com.dft.netsuite.handler;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.services.SignatureService;

public class NSApi extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return null;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return null;
    }

    @Override
    public SignatureService getSignatureService() {
        return new HMACSha256SignatureService();
    }
}
