package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentList {

    @SerializedName("reply")
    @Expose
    private java.util.List<Reply> reply = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("like_status")
    @Expose
    private Boolean likeStatus;
    @SerializedName("timeago")
    @Expose
    private String timeago;
    @SerializedName("createdon")
    @Expose
    private String createdon;

    public List<Reply> getReply() {
        return reply;
    }

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public Boolean getLikeStatus() {
        return likeStatus;
    }

    public String getTimeago() {
        return timeago;
    }

    public String getCreatedon() {
        return createdon;
    }
}
