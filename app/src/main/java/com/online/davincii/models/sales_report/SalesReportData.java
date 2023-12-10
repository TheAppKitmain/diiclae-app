package com.online.davincii.models.sales_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReportData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("by_image")
    @Expose
    private String byImage;
    @SerializedName("canvasimage")
    @Expose
    private String canvasimage;
    @SerializedName("by_name")
    @Expose
    private String byName;
    @SerializedName("by_userid")
    @Expose
    private String byUserid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("canvas_id")
    @Expose
    private Integer canvasId;
    @SerializedName("canvas_type_id")
    @Expose
    private String canvasTypeId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timeago")
    @Expose
    private String timeago;
    @SerializedName("support")
    @Expose
    private Boolean support;
    @SerializedName("createdon")
    @Expose
    private String createdon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getByImage() {
        return byImage;
    }

    public void setByImage(String byImage) {
        this.byImage = byImage;
    }

    public String getCanvasimage() {
        return canvasimage;
    }

    public void setCanvasimage(String canvasimage) {
        this.canvasimage = canvasimage;
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public String getByUserid() {
        return byUserid;
    }

    public void setByUserid(String byUserid) {
        this.byUserid = byUserid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(Integer canvasId) {
        this.canvasId = canvasId;
    }

    public String getCanvasTypeId() {
        return canvasTypeId;
    }

    public void setCanvasTypeId(String canvasTypeId) {
        this.canvasTypeId = canvasTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }

    public Boolean getSupport() {
        return support;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }
}
