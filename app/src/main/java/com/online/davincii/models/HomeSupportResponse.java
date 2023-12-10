package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeSupportResponse {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
