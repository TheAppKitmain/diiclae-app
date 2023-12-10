package com.online.davincii.models.sales_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("canvasid")
    @Expose
    private Integer canvasid;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("reported_by")
    @Expose
    private String reportedBy;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCanvasid() {
        return canvasid;
    }

    public void setCanvasid(Integer canvasid) {
        this.canvasid = canvasid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
