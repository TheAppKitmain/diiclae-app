package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AddCreationData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("creator_id")
    @Expose
    public String creatorId;
    @SerializedName("canvas_id")
    @Expose
    public Integer canvasId;
    @SerializedName("canvas_type")
    @Expose
    public String canvasType;
    @SerializedName("picture_url")
    @Expose
    public String pictureUrl;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("style")
    @Expose
    public String style;
    @SerializedName("hashtag_1")
    @Expose
    public String hashtag1;
    @SerializedName("hashtag_2")
    @Expose
    public String hashtag2;
    @SerializedName("hashtag_3")
    @Expose
    public String hashtag3;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;

    public Integer getId() {
        return id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public Integer getCanvasId() {
        return canvasId;
    }

    public String getCanvasType() {
        return canvasType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getStyle() {
        return style;
    }

    public String getHashtag1() {
        return hashtag1;
    }

    public String getHashtag2() {
        return hashtag2;
    }

    public String getHashtag3() {
        return hashtag3;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("createdOn")
    @Expose
    public String createdOn;

}
