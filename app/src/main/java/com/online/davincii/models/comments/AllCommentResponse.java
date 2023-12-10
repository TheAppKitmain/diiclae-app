package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCommentResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private List<CommentList> data = null;
    @SerializedName("total_Pages")
    @Expose
    private Integer totalPages;
    @SerializedName("next_page")
    @Expose
    private Integer nextPage;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public List<CommentList> getData() {
        return data;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public String getMessage() {
        return message;
    }
}
