package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudioprofileResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("pieces")
    @Expose
    private Integer pieces;
    @SerializedName("data")
    @Expose
    private StudioprofileData data;
    @SerializedName("creator")
    @Expose
    private List<StudioprofileCreator> creator = null;
    @SerializedName("collectionlist")
    @Expose
    private List<StudioCollectionList> collectionlist = null;
    @SerializedName("supported")
    @Expose
    private String supported;
    @SerializedName("supporting")
    @Expose
    private String supporting;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("support")
    @Expose
    private boolean support;


    public boolean isSupport() {
        return support;
    }

    public Integer getPieces() {
        return pieces;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public StudioprofileData getData() {
        return data;
    }

    public void setData(StudioprofileData data) {
        this.data = data;
    }

    public List<StudioprofileCreator> getCreator() {
        return creator;
    }

    public void setCreator(List<StudioprofileCreator> creator) {
        this.creator = creator;
    }

    public List<StudioCollectionList> getCollectionlist() {
        return collectionlist;
    }

    public void setCollectionlist(List<StudioCollectionList> collectionlist) {
        this.collectionlist = collectionlist;
    }

    public String getSupported() {
        return supported;
    }

    public void setSupported(String supported) {
        this.supported = supported;
    }

    public String getSupporting() {
        return supporting;
    }

    public void setSupporting(String supporting) {
        this.supporting = supporting;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}