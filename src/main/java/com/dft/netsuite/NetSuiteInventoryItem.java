package com.dft.netsuite;

import com.dft.netsuite.handler.JsonBodyHandler;
import com.dft.netsuite.model.credentials.Credentials;
import com.dft.netsuite.model.inventoryItem.InventoryItemRoot;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static com.dft.netsuite.constantcodes.ConstantCode.INVENTORY_ITEM_ENDPOINT;

public class NetSuiteInventoryItem extends NetSuiteRestSdk {

    private final String domain;

    public NetSuiteInventoryItem(Credentials credentials) {
        super(credentials);
        this.domain = "https://" + this.credentials.getInstanceId() + ".suitetalk.api.netsuite.com";
    }

    @SneakyThrows
    public InventoryItemRoot getInventoryItems(HashMap<String, String> param) {
        getAccessCredentials();
        URI uri = addParameters(URI.create(domain + INVENTORY_ITEM_ENDPOINT), param);
        HttpRequest request = get(uri);
        HttpResponse.BodyHandler<InventoryItemRoot> handler = new JsonBodyHandler<>(InventoryItemRoot.class);
        return getRequestWrapped(request, handler);
    }
}