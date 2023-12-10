package com.online.davincii.models.discoverydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("userid")
    @Expose
    public String userid;
    @SerializedName("profileimage")
    @Expose
    public String profileimage;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("support")
    @Expose
    public Boolean support;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("timeago")
    @Expose
    public String timeago;
    @SerializedName("canvasimage")
    @Expose
    public String canvasimage;
    @SerializedName("like")
    @Expose
    public Boolean like;
    @SerializedName("canvas_id")
    @Expose
    public Integer canvasId;
    @SerializedName("createdon")
    @Expose
    public String createdon;

    public Integer getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getSupport() {
        return support;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeago() {
        return timeago;
    }

    public String getCanvasimage() {
        return canvasimage;
    }

    public Boolean getLike() {
        return like;
    }

    public Integer getCanvasId() {
        return canvasId;
    }

    public String getCreatedon() {
        return createdon;
    }
}
