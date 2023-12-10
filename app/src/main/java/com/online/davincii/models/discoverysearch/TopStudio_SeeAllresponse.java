package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopStudio_SeeAllresponse {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("imageurl")
    @Expose
    public String imageurl;
    @SerializedName("createdon")
    @Expose
    public String createdon;

    public String getId() {
        return id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getCreatedon() {
        return createdon;
    }
}
