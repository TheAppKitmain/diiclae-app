package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyCreationRequest {
    @SerializedName("id")
    @Expose
    public Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    public BuyCreationRequest(int id) {
        this.id = id;
    }
}
