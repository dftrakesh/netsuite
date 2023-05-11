package com.dft.netsuite.constantcodes;

public interface ConstantCode {

    String AUTHORIZE_ENDPOINT = "/app/login/oauth2/authorize.nl";
    String TOKEN_ENDPOINT = "/services/rest/auth/oauth2/v1/token";
    String INVENTORY_ITEM_ENDPOINT = "/services/rest/record/v1/inventoryItem";
    String INVOICE_ENDPOINT = "/services/rest/record/v1/invoice";
    String CONTENT_TYPE ="Content-Type";
    String X_WWW_FORM_URLENCODED ="application/x-www-form-urlencoded";
    String AUTHORIZATION ="Authorization";
    String BEARER ="Bearer ";
    String ACCEPT ="Accept";
    String APPLICATION_JSON ="application/json";
}