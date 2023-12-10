package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeLikeResponse {
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("isLiked")
    @Expose
    public Boolean isLiked;

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
