package com.online.davincii.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class MyCartRequest {


    @SerializedName("creation_id")
    @Expose
    private Integer creationId;
    @SerializedName("canvas_size_id")
    @Expose
    private Integer canvasSizeId;

    public MyCartRequest(Integer creationId, Integer canvasSizeId) {
        this.creationId = creationId;
        this.canvasSizeId = canvasSizeId;
    }

    public Integer getCreationId() {
        return creationId;
    }

    public void setCreationId(Integer creationId) {
        this.creationId = creationId;
    }

    public Integer getCanvasSizeId() {
        return canvasSizeId;
    }

    public void setCanvasSizeId(Integer canvasSizeId) {
        this.canvasSizeId = canvasSizeId;
    }
}
