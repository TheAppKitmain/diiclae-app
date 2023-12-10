package com.online.davincii.models.sales_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportRequest {
    @SerializedName("canvasid")
    @Expose
    private Integer canvasid;
    @SerializedName("optionid")
    @Expose
    private Integer optionid;

    public ReportRequest(Integer canvasid, Integer optionid) {
        this.canvasid = canvasid;
        this.optionid = optionid;
    }

    public Integer getCanvasid() {
        return canvasid;
    }

    public void setCanvasid(Integer canvasid) {
        this.canvasid = canvasid;
    }

    public Integer getOptionid() {
        return optionid;
    }

    public void setOptionid(Integer optionid) {
        this.optionid = optionid;
    }
}
