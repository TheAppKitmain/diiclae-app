package com.online.davincii.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("delivery_address_id")
    @Expose
    private Integer deliveryAddressId;
    @SerializedName("payment_nounce")
    @Expose
    private String paymentNounce;

    public OrderRequest(Integer deliveryAddressId, String paymentNounce) {
        this.deliveryAddressId = deliveryAddressId;
        this.paymentNounce = paymentNounce;
    }
}
