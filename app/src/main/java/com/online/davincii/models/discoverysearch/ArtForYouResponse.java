package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtForYouResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("createdon")
    @Expose
    private String createdon;
    @SerializedName("canvastype")
    @Expose
    private Integer canvastype;

    public Integer getId() {
        return id;
    }
    public String getImageurl() {
        return imageurl;
    }
    public String getUserid() {
        return userid;
    }
    public String getCreatedon() {
        return createdon;
    }
    public Integer getCanvastype() {
        return canvastype;
    }
}
