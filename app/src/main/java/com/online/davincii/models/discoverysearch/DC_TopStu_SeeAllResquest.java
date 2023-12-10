package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DC_TopStu_SeeAllResquest {
    @SerializedName("pageno")
    @Expose
    public Integer pageno;

    public DC_TopStu_SeeAllResquest(Integer pageno) {
        this.pageno = pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }
}
