package com.cliquet.gautier.go4lunch.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    public String getVicinity() { return vicinity; }
    public void setVicinity(String vicinity) { this.vicinity = vicinity; }

    @SerializedName("geometry")
    @Expose
    private Geometry geometry = null;

    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours = null;

    public OpeningHours getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }
}
