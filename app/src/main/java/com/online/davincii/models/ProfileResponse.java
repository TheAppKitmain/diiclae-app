package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private UserProfileData data;
    @SerializedName("categories")
    @Expose
    private List<ProfileCategories> categories = null;
    @SerializedName("styles")
    @Expose
    private List<ProfileStyles> styles = null;
    @SerializedName("creator")
    @Expose
    private List<ProfileCreator> creator = null;
    @SerializedName("collection")
    @Expose
    private List<ProfileCollections> collection = null;
    @SerializedName("peices")
    @Expose
    private Integer peices;
    @SerializedName("supported")
    @Expose
    private Integer supported;
    @SerializedName("supporting")
    @Expose
    private Integer supporting;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UserProfileData getData() {
        return data;
    }

    public void setData(UserProfileData data) {
        this.data = data;
    }

    public List<ProfileCategories> getCategories() {
        return categories;
    }

    public void setCategories(List<ProfileCategories> categories) {
        this.categories = categories;
    }

    public List<ProfileStyles> getStyles() {
        return styles;
    }

    public void setStyles(List<ProfileStyles> styles) {
        this.styles = styles;
    }

    public List<ProfileCreator> getCreator() {
        return creator;
    }

    public void setCreator(List<ProfileCreator> creator) {
        this.creator = creator;
    }

    public List<ProfileCollections> getCollection() {
        return collection;
    }

    public void setCollection(List<ProfileCollections> collection) {
        this.collection = collection;
    }

    public Integer getPeices() {
        return peices;
    }

    public void setPeices(Integer peices) {
        this.peices = peices;
    }

    public Integer getSupported() {
        return supported;
    }

    public void setSupported(Integer supported) {
        this.supported = supported;
    }

    public Integer getSupporting() {
        return supporting;
    }

    public void setSupporting(Integer supporting) {
        this.supporting = supporting;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
