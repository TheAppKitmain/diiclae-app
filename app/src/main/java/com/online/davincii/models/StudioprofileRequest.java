package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudioprofileRequest{


    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
