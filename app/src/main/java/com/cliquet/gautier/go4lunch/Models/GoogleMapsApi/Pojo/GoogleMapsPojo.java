package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapsPojo {
    @SerializedName("results")
    @Expose
    private List<NearbySearchResults> nearbySearchResults;

    public List<NearbySearchResults> getNearbySearchResults() {
        return nearbySearchResults;
    }
    public void setNearbySearchResults(List<NearbySearchResults> nearbySearchResults) {
        this.nearbySearchResults = nearbySearchResults;
    }


    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    public String getNextPageToken() { return nextPageToken; }
    public void setNextPageToken(String nextPageToken) { this.nextPageToken = nextPageToken; }
}
