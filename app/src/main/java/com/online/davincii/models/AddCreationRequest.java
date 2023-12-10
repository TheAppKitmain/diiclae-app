package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddCreationRequest {

    @SerializedName("canvasid")
    @Expose
    private String canvasid;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("hashtag1")
    @Expose
    private String hashtag1;
    @SerializedName("style")
    @Expose
    private String style;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("declaration")
    @Expose
    private Boolean declaration;
    @SerializedName("sizes")
    @Expose
    private List<PriceData> sizes = null;


    public AddCreationRequest(String canvasid, String image, String hashtag1, String style, String title, String description, String category, Boolean declaration, List<PriceData> sizes) {
        this.canvasid = canvasid;
        this.image = image;
        this.hashtag1 = hashtag1;
        this.style = style;
        this.title = title;
        this.description = description;
        this.category = category;
        this.declaration = declaration;
        this.sizes = sizes;
    }

    public String getCanvasid() {
        return canvasid;
    }

    public void setCanvasid(String canvasid) {
        this.canvasid = canvasid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHashtag1() {
        return hashtag1;
    }

    public void setHashtag1(String hashtag1) {
        this.hashtag1 = hashtag1;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Boolean declaration) {
        this.declaration = declaration;
    }

    public List<PriceData> getSizes() {
        return sizes;
    }

    public void setSizes(List<PriceData> sizes) {
        this.sizes = sizes;
    }

}
