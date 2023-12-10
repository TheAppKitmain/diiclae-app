package com.online.davincii.models.discoverydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscoveryRequest {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("pageno")
    @Expose
    public Integer pageno;

    public DiscoveryRequest(Integer id, Integer pageno) {
        this.id = id;
        this.pageno = pageno;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }
}
