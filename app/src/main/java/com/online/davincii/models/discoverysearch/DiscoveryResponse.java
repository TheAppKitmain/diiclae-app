package com.online.davincii.models.discoverysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscoveryResponse {


    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("top_Studios")
    @Expose
    public List<TopStudioResponse> topStudios = null;
    @SerializedName("art_for_you")
    @Expose
    public List<ArtForYouResponse> artForYou = null;
    @SerializedName("culturedart")
    @Expose
    public List<CulturedArtResponse> culturedart = null;
    @SerializedName("comicArt")
    @Expose
    public List<ComicArtResponse> comicArt = null;
    @SerializedName("top_10_pieces")
    @Expose
    public List<TopTenPieceResponse> toptenPieces = null;
    @SerializedName("featuredArtist")
    @Expose
    public List<FeaturedArtistResponse> featuredArtist = null;
    @SerializedName("recentlyLoved")
    @Expose
    public List<RecentlyLovedResponse> recentlyLoved = null;
    @SerializedName("message")
    @Expose
    public String message;

    public String getError() {
        return error;
    }

    public List<TopStudioResponse> getTopStudios() {
        return topStudios;
    }

    public List<ArtForYouResponse> getArtForYou() {
        return artForYou;
    }

    public List<CulturedArtResponse> getCulturedart() {
        return culturedart;
    }

    public List<ComicArtResponse> getComicArt() {
        return comicArt;
    }

    public List<TopTenPieceResponse> getTopTenPieces() {
        return toptenPieces;
    }

    public List<FeaturedArtistResponse> getFeaturedArtist() {
        return featuredArtist;
    }

    public List<RecentlyLovedResponse> getRecentlyLoved() {
        return recentlyLoved;
    }

    public String getMessage() {
        return message;
    }
}
