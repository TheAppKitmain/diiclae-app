package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class PieceSearch implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("imageurl")
    @Expose
    public String imageurl;
    @SerializedName("userid")
    @Expose
    public String userid;
    @SerializedName("support")
    @Expose
    public Boolean support;
    @SerializedName("createdon")
    @Expose
    public String createdon;
    @SerializedName("canvastype")
    @Expose
    public Integer canvastype;

    public Integer getId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public Boolean getSupport() {
        return support;
    }

    public String getCreatedon() {
        return createdon;
    }

    public Integer getCanvastype() {
        return canvastype;
    }
}
