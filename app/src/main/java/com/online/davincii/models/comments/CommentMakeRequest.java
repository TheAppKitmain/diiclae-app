package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentMakeRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message")
    @Expose
    private String message;

    public CommentMakeRequest(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
