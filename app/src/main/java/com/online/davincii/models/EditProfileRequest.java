package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProfileRequest {

    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("profilepic")
    @Expose
    private String profilepic;
    @SerializedName("studiopic")
    @Expose
    private String studiopic;
    @SerializedName("category")
    @Expose
    private List<Integer> category = null;
    @SerializedName("styles")
    @Expose
    private List<Integer> styles = null;



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getStudiopic() {
        return studiopic;
    }

    public void setStudiopic(String studiopic) {
        this.studiopic = studiopic;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public List<Integer> getStyles() {
        return styles;
    }

    public void setStyles(List<Integer> styles) {
        this.styles = styles;
    }
}
