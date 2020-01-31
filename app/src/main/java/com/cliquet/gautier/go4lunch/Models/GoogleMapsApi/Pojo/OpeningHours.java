package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import android.content.PeriodicSync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private boolean openNow;

    @SerializedName("periods")
    @Expose
    private List<Periods> periods;

    public boolean getOpenNow() { return openNow; }
    public void setOpenNow(boolean openNow) { this.openNow = openNow; }

    public List<Periods> getPeriods() {
        return periods;
    }
    public void setPeriods(List<Periods> periods) {
        this.periods = periods;
    }
}
