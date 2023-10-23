package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceRequest {

    private IdFiled customform;
    private IdFiled account;
    private IdFiled currency;
    private IdFiled entity;

    @JsonProperty("custbody_njt_ecommerce_id")
    private String ecommerceId;

    private String otherRefNum;
    private String terms;

    @JsonProperty("custbody_njt_incoterms")
    private String custbodyNjtIncoterms;

    private IdFiled department;

    @JsonProperty("custbody_njt_seller_cloud_order_id")
    private String sellerCloudOrderId;

    @JsonProperty("custbody_njt_entry_source")
    private String entrySource;

    private ItemRoot item;
    private IdFiled location;
    private String salesEffectiveDate;
    private String shipDate;
    private IdFiled shipMethod;
    private IdFiled status;
    private IdFiled subsidiary;
    private Double subtotal;
    private TaxDetails taxDetails;
    private String trackingNumbers;
    private Boolean taxDetailsOverride;
    private Double total;
    private Double totalAfterTaxes;
    private Double totalCostEstimate;
    private String tranDate;
}