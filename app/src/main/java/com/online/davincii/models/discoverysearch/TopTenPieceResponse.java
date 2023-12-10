package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTenPieceResponse {

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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public Integer getCanvastype() {
        return canvastype;
    }

    public void setCanvastype(Integer canvastype) {
        this.canvastype = canvastype;
    }
}
