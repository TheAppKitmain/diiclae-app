package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class AddCreationResponse {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public AddCreationData data;
    @SerializedName("usercategory")
    @Expose
    public List<UserProfileCategory> usercategory = null;
    @SerializedName("userstyle")
    @Expose
    public List<UserProfileStyle> userstyle = null;

    public String getError() {
        return error;
    }

    public AddCreationData getData() {
        return data;
    }

    public List<UserProfileCategory> getUsercategory() {
        return usercategory;
    }

    public List<UserProfileStyle> getUserstyle() {
        return userstyle;
    }

    public String getMessage() {
        return message;
    }

    @SerializedName("message")
    @Expose
    public String message;
}
