package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedRequest {
    @SerializedName("pageno")
    @Expose
    private Integer pageno;

    @SerializedName("id")
    @Expose
    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public FeedRequest(Integer pageno) {
        this.pageno = pageno;
    }
    public FeedRequest() {
        this.pageno = pageno;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

}
