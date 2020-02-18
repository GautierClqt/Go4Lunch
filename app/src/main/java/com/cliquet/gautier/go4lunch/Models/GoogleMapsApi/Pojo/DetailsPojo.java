package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsPojo {
    @SerializedName("result")
    @Expose
    private Results results;

    public Results getResults() { return results; }
    public void setResults(Results results) {
        this.results = results;
    }

    public class Results {
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours = null;

        @SerializedName("international_phone_number")
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

    public class Periods {
        @SerializedName("open")
        @Expose
        private Open open;

        @SerializedName("close")
        @Expose
        private Close close;

        public Open getOpen() {
            return open;
        }

        public void setOpen(Open open) {
            this.open = open;
        }

        public Close getClose() {
            return close;
        }

        public void setClose(Close close) {
            this.close = close;
        }
    }

    public class Open {
        @SerializedName("day")
        @Expose
        private int day;

        @SerializedName("time")
        @Expose
        private String time;

        public int getDay() {
            return day;
        }
        public void setDay(int day) {
            this.day = day;
        }

        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
    }

    public class Close {
        @SerializedName("day")
        @Expose
        private int day;

        @SerializedName("time")
        @Expose
        private String time;

        public int getDay() {
            return day;
        }
        public void setDay(int day) {
            this.day = day;
        }

        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
    }
}
