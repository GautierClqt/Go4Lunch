package com.cliquet.gautier.go4lunch.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapsPojo {
    @SerializedName("results")
    @Expose
    private List<Results> results;

    public List<Results> getResults() {
        int TEST = 400;
        return results;
    }
    public void setResults(List<Results> results) {
        this.results = results;
        int test = 300;
    }


    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    public String getNextPageToken() { return nextPageToken; }
    public void setNextPageToken(String nextPageToken) { this.nextPageToken = nextPageToken; }
}
