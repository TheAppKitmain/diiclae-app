package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private OrderDetailData data;
    @SerializedName("cart")
    @Expose
    private List<OrderCart> cart = null;
    @SerializedName("address")
    @Expose
    private OrderAddress address;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OrderDetailData getData() {
        return data;
    }

    public void setData(OrderDetailData data) {
        this.data = data;
    }

    public List<OrderCart> getCart() {
        return cart;
    }

    public void setCart(List<OrderCart> cart) {
        this.cart = cart;
    }

    public OrderAddress getAddress() {
        return address;
    }

    public void setAddress(OrderAddress address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
