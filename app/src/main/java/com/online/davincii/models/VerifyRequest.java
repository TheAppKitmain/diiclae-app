package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyRequest {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("code")
    @Expose
    public String code;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
