package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DC_ArtFu_SeeAllRequest {
    @SerializedName("pageno")
    @Expose
    public Integer pageno;

    public DC_ArtFu_SeeAllRequest(Integer pageno) {
        this.pageno = pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }
}
