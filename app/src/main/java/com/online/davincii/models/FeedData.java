package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedData {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("views")
    @Expose
    private Integer views;

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("support")
    @Expose
    private Boolean support;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timeago")
    @Expose
    private String timeago;
    @SerializedName("canvasimage")
    @Expose
    private String canvasimage;
    @SerializedName("like")
    @Expose
    private Boolean like;
    @SerializedName("canvas_id")
    @Expose
    private String canvasId;
    @SerializedName("createdon")
    @Expose
    private String createdon;
    @SerializedName("report")
    @Expose
    private Boolean report;

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

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

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSupport() {
        return support;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }

    public String getCanvasimage() {
        return canvasimage;
    }

    public void setCanvasimage(String canvasimage) {
        this.canvasimage = canvasimage;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public String getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(String canvasId) {
        this.canvasId = canvasId;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }
}
