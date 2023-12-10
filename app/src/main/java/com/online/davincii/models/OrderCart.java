package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderCart {
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("canvas_size")
    @Expose
    private String canvasSize;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("totalprice")
    @Expose
    private Double totalprice;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("cartid")
    @Expose
    private Integer cartid;
    @SerializedName("canvas_id")
    @Expose
    private Integer canvasId;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("canvas_type")
    @Expose
    private String canvasType;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("deliverydate")
    @Expose
    private String deliverydate;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCanvasSize() {
        return canvasSize;
    }

    public void setCanvasSize(String canvasSize) {
        this.canvasSize = canvasSize;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCartid() {
        return cartid;
    }

    public void setCartid(Integer cartid) {
        this.cartid = cartid;
    }

    public Integer getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(Integer canvasId) {
        this.canvasId = canvasId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCanvasType() {
        return canvasType;
    }

    public void setCanvasType(String canvasType) {
        this.canvasType = canvasType;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public String getDeliverydate() {
        return deliverydate;
    }
}
