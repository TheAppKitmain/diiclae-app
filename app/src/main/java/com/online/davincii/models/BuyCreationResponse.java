package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuyCreationResponse {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public BuyCreationData data;
    @SerializedName("canvassizes")
    @Expose
    public List<BuyCreationCanvasSize> canvassizes = null;
    @SerializedName("nft")
    @Expose
    private Boolean nft;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public BuyCreationData getData() {
        return data;
    }

    public List<BuyCreationCanvasSize> getCanvassizes() {
        return canvassizes;
    }

    public Boolean getNft() {
        return nft;
    }

    public String getMessage() {
        return message;
    }
}
