package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CanvasSizeData {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("canvas_type")
    @Expose
    private String canvasType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCanvasType() {
        return canvasType;
    }

    public void setCanvasType(String canvasType) {
        this.canvasType = canvasType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
