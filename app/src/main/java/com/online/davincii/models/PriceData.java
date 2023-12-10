package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceData {
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("size_id")
    @Expose
    private Long sizeId;

    public PriceData(String size, Double price, Long sizeId) {
        this.size = size;
        this.price = price;
        this.sizeId = sizeId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }
}
