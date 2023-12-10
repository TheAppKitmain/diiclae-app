package com.online.davincii.models.registger;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("user_profile")
    @Expose
    private UserProfile userProfile;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_categorylist")
    @Expose
    private List<UserCategorylist> userCategorylist = null;
    @SerializedName("user_stylelist")
    @Expose
    private List<UserStylelist> userStylelist = null;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("message")
    @Expose
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserCategorylist> getUserCategorylist() {
        return userCategorylist;
    }

    public void setUserCategorylist(List<UserCategorylist> userCategorylist) {
        this.userCategorylist = userCategorylist;
    }

    public List<UserStylelist> getUserStylelist() {
        return userStylelist;
    }

    public void setUserStylelist(List<UserStylelist> userStylelist) {
        this.userStylelist = userStylelist;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}