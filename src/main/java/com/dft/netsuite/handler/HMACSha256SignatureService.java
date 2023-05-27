package com.dft.netsuite.handler;

import com.github.scribejava.core.exceptions.OAuthSignatureException;
import com.github.scribejava.core.services.SignatureService;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class HMACSha256SignatureService implements SignatureService {
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String METHOD = "HMAC-SHA256";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | RuntimeException e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeyException {
        final SecretKeySpec key = new SecretKeySpec(keyString.getBytes(UTF8), HMAC_SHA256);
        final Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(key);
        final byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    private String bytesToBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSignatureMethod() {
        return METHOD;
    }
}
