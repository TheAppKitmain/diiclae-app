package com.online.davincii.models.registger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("s3_KEY")
    @Expose
    public String s3KEY;
    @SerializedName("secret")
    @Expose
    public String secret;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public String getS3KEY() {
        return s3KEY;
    }

    public String getSecret() {
        return secret;
    }

    public String getMessage() {
        return message;
    }
}
