package com.online.davincii.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private OrderData data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public OrderData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
