package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudioSearch {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("imageurl")
    @Expose
    public String imageurl;
    @SerializedName("createdon")
    @Expose
    public String createdon;
    @SerializedName("canvastype")
    @Expose
    public Integer canvastype;

    public String getId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getCreatedon() {
        return createdon;
    }

    public Integer getCanvastype() {
        return canvastype;
    }
}