package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCommentsRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pageno")
    @Expose
    private Integer pageno;

    public AllCommentsRequest(Integer id, Integer pageno) {
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
