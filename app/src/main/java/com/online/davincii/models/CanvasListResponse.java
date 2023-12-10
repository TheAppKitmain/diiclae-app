package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CanvasListResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("selected_creation")
    @Expose
    private List<SelectedCreation> selectedCreation = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SelectedCreation> getSelectedCreation() {
        return selectedCreation;
    }

    public void setSelectedCreation(List<SelectedCreation> selectedCreation) {
        this.selectedCreation = selectedCreation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
