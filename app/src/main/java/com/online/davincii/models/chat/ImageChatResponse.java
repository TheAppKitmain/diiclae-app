package com.online.davincii.models.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageChatResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imageresponse")
    @Expose
    private String imageresponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageresponse() {
        return imageresponse;
    }

    public void setImageresponse(String imageresponse) {
        this.imageresponse = imageresponse;
    }

}
