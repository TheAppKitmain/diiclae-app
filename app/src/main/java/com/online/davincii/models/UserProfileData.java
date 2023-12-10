package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class UserProfileData implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("userid")
    @Expose
    public String userid;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
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
    @SerializedName("bio")
    @Expose
    public String bio;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("createdOn")
    @Expose
    public String createdOn;

    public Integer getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getCountry() {
        return country;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getStudioPicture() {
        return studioPicture;
    }

    public String getBio() {
        return bio;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
