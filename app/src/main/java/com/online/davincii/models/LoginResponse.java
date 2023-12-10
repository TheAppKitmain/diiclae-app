package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("userid")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("profile_picture")
    @Expose
    public String profile_picture;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
