package com.online.davincii.models.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {

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
    @SerializedName("replyby")
    @Expose
    private String replyby;
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

    public String getReplyby() {
        return replyby;
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
