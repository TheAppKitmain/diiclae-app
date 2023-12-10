package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterRequest {

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("dob")
    @Expose
    public String dob;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("profile_picture")
    @Expose
    public String profilePicture;
    @SerializedName("studio_picture")
    @Expose
    public String studioPicture;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("category")
    @Expose
    public List<Integer> category = null;
    @SerializedName("styles")
    @Expose
    public List<Integer> styles = null;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_id")
    @Expose
    private String deviceId;

    public RegisterRequest(String firstName, String lastName, String email, String username, String dob, String country, String profilePicture, String studioPicture, String password, List<Integer> category, List<Integer> styles, String deviceToken, String deviceType, String deviceId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.dob = dob;
        this.country = country;
        this.profilePicture = profilePicture;
        this.studioPicture = studioPicture;
        this.password = password;
        this.category = category;
        this.styles = styles;
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
    }
}
