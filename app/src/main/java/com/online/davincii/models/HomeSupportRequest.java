package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeSupportRequest {

    @SerializedName("id")
    @Expose
    public String id;

    public HomeSupportRequest(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
