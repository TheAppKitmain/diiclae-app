package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StyleListResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("created_on")
    @Expose
    public String createdOn;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}