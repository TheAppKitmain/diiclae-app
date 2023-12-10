package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.online.davincii.models.order.OrderData;

import java.util.List;

public class OrderListResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("orders")
    @Expose
    private List<OrderListData> orders = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<OrderListData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderListData> orders) {
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
