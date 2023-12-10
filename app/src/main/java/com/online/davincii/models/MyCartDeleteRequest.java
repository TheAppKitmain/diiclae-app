package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class MyCartDeleteRequest {
    @SerializedName("id")
    @Expose
    private Integer id;

    public MyCartDeleteRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

