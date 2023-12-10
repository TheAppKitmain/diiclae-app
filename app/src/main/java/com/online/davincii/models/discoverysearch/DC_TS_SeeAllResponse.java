package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DC_TS_SeeAllResponse {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public List<TopStudio_SeeAllresponse> data = null;
    @SerializedName("total_Pages")
    @Expose
    public Integer totalPages;
    @SerializedName("next_page")
    @Expose
    public Integer nextPage;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public List<TopStudio_SeeAllresponse> getData() {
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

