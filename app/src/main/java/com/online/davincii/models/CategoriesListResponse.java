package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesListResponse {
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("categorylist")
    @Expose
    public List<CategorylistResponse> categorylistResponse = null;
    @SerializedName("stylelist")
    @Expose
    public List<StyleListResponse> styleListResponse = null;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public List<CategorylistResponse> getCategorylistResponse() {
        return categorylistResponse;
    }

    public List<StyleListResponse> getStyleListResponse() {
        return styleListResponse;
    }

    public String getMessage() {
        return message;
    }
}
