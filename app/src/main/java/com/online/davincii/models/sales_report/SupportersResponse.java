package com.online.davincii.models.sales_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.online.davincii.models.SupportersData;

import java.util.List;

public class SupportersResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("supporters")
    @Expose
    private List<SupportersData> supporters = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SupportersData> getSupporters() {
        return supporters;
    }

    public void setSupporters(List<SupportersData> supporters) {
        this.supporters = supporters;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
