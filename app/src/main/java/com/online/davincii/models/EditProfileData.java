package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class EditProfileData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("studio_picture")
    @Expose
    private String studioPicture;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

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
