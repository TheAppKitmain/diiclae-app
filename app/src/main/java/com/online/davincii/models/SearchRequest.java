package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequest {
    @SerializedName("search")
    @Expose
    public String search;

    public SearchRequest(String search) {
        this.search = search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
