package com.cliquet.gautier.go4lunch.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private boolean openNow;

    public boolean getOpenNow() { return openNow; }
    public void setOpenNow(boolean openNow) { this.openNow = openNow; }

}
