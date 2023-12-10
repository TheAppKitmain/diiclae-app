package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class MyCartResponse{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("total_Price")
    @Expose
    private Double totalPrice;
    @SerializedName("sub_Total")
    @Expose
    private Double subTotal;
    @SerializedName("delivery_Charge")
    @Expose
    private Integer deliveryCharge;
    @SerializedName("cart_items")
    @Expose
    private List<MyCartData> cartItems = null;
    @SerializedName("cart_count")
    @Expose
    private Integer cartCount;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public List<MyCartData> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<MyCartData> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
