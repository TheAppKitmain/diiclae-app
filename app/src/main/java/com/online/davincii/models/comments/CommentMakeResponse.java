package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentMakeResponse {


    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("list")
    @Expose
    private java.util.List<CommentList> list = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public List<CommentList> getList() {
        return list;
    }

    public String getMessage() {
        return message;
    }
}
