package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class MyCartDeleteResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("my_Cart")
    @Expose
    private List<MyCartData> myCart = null;
    @SerializedName("total_Price")
    @Expose
    private Double totalPrice;
    @SerializedName("sub_Total")
    @Expose
    private Double subTotal;
    @SerializedName("delivery_Charge")
    @Expose
    private Integer deliveryCharge;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<MyCartData> getMyCart() {
        return myCart;
    }

    public void setMyCart(List<MyCartData> myCart) {
        this.myCart = myCart;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Integer deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
