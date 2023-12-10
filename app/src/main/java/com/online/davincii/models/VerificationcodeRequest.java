package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerificationcodeRequest {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("code")
    @Expose
    private String code;

    public VerificationcodeRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
