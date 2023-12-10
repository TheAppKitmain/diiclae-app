package com.online.davincii.models.registger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtFu_SeeAllResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("imageurl")
    @Expose
    public String imageurl;
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

    public String getCreatedon() {
        return createdon;
    }

    public Integer getCanvastype() {
        return canvastype;
    }
}
