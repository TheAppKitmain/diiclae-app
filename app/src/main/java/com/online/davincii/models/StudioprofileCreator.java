package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class StudioprofileCreator {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("createdon")
    @Expose
    private String createdon;
    @SerializedName("canvastype")
    @Expose
    private Integer canvastype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public Integer getCanvastype() {
        return canvastype;
    }

    public void setCanvastype(Integer canvastype) {
        this.canvastype = canvastype;
    }
}
