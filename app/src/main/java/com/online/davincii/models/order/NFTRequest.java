package com.online.davincii.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NFTRequest {
    @SerializedName("creation_id")
    @Expose
    private Integer creationId;
    @SerializedName("payment_nounce")
    @Expose
    private String paymentNounce;

    public NFTRequest(Integer creationId, String paymentNounce) {
        this.creationId = creationId;
        this.paymentNounce = paymentNounce;
    }
}
