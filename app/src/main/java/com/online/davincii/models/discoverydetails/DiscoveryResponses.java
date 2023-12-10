package com.online.davincii.models.discoverydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.online.davincii.models.FeedData;

import java.util.List;

public class DiscoveryResponses {
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public List<FeedData> data = null;
    @SerializedName("selected_Canvas")
    @Expose
    public FeedData selectedCanvas;
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

    public List<FeedData> getData() {
        return data;
    }

    public FeedData getSelectedCanvas() {
        return selectedCanvas;
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
