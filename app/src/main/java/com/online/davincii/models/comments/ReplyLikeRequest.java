package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyLikeRequest {
    @SerializedName("id")
    @Expose
    private Integer id;

    public ReplyLikeRequest(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
