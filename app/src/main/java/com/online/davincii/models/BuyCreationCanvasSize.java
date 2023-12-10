package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyCreationCanvasSize {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("price")
    @Expose
    public Float price;
    @SerializedName("canvas_type")
    @Expose
    public String canvasType;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("createdOn")
    @Expose
    public String createdOn;

    public Integer getId() {
        return id;
    }

    public String getSize() {
        return size;
    }

    public Float getPrice() {
        return price;
    }

    public String getCanvasType() {
        return canvasType;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }


}
