package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
}
