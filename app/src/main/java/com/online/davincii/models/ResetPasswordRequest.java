package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("password")
    @Expose
    private String password;

    public ResetPasswordRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }
}
