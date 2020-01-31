package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//DetailsResults stores the respons of the GoogleMaps Details Api
public class DetailsResults {
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours = null;

    public OpeningHours getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }
}
