package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentLikeResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("isLiked")
    @Expose
    private Boolean isLiked;

    public String getError() {
        return error;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getLiked() {
        return isLiked;
    }
}
