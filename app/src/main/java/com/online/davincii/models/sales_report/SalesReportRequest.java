package com.online.davincii.models.sales_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReportRequest {
    @SerializedName("pageno")
    @Expose
    private Integer pageno;

    public SalesReportRequest(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

}
