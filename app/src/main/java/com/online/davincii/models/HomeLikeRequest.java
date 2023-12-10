package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeLikeRequest {

    @SerializedName("id")
    @Expose
    public Integer id;

    public HomeLikeRequest(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
