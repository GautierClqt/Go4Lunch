package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//DetailsResults stores the respons of the GoogleMaps Details Api
public class DetailsResults {
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours = null;

    @SerializedName("formatted_phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("website")
    @Expose
    private String website;

    public OpeningHours getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() { return website; }
    public void setWebsite(String website) {
        this.phoneNumber = phoneNumber;
    }
}
