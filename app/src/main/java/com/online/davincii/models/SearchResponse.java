package com.online.davincii.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("studios")
    @Expose
    public List<StudioSearch> studios = null;
    @SerializedName("pieces")
    @Expose
    public List<PieceSearch> pieces = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("recommended")
    @Expose
    public String recommended;

    public String getError() {
        return error;
    }

    public List<StudioSearch> getStudios() {
        return studios;
    }

    public List<PieceSearch> getPieces() {
        return pieces;
    }

    public String getMessage() {
        return message;
    }

    public String getRecommended() {
        return recommended;
    }
}