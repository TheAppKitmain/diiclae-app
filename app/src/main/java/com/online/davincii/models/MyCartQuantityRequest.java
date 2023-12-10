package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class MyCartQuantityRequest {

    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("cart_id")
    @Expose
    private Integer cartId;

    public MyCartQuantityRequest(Integer quantity, Integer cartId) {
        this.quantity = quantity;
        this.cartId = cartId;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

}
