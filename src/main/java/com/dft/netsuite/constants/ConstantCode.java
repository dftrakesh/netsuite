package com.dft.netsuite.constants;

public interface ConstantCode {

    String AUTHORIZE_ENDPOINT = "/app/login/oauth2/authorize.nl";
    String TOKEN_ENDPOINT = "/services/rest/auth/oauth2/v1/token";
    String INVOICE_ENDPOINT = "/services/rest/record/v1/invoice";
    String CUSTOMER_PAYMENT_ENDPOINT = "/services/rest/record/v1/customerPayment";
    String CONTENT_TYPE ="Content-Type";
    String X_WWW_FORM_URLENCODED ="application/x-www-form-urlencoded";
    String AUTHORIZATION ="Authorization";
    String OAUTH_VERSION ="1.0";
    String BEARER ="Bearer ";
    String ACCEPT ="Accept";
    String APPLICATION_JSON ="application/json";
    String SUCCESS ="SUCCESS";
}