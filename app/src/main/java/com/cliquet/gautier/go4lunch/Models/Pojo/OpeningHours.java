package com.cliquet.gautier.go4lunch.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private String openNow;

    public String getOpenNow() { return openNow; }
    public void setOpenNow(String openNow) { this.openNow = openNow; }

}
