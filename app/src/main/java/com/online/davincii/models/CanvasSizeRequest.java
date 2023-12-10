package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CanvasSizeRequest {
    @SerializedName("id")
    @Expose
    private Integer id;

    public CanvasSizeRequest(Integer id) {
        this.id = id;
    }
}
