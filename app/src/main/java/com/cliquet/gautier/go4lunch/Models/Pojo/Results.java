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

    @SerializedName("geometry")
    @Expose
    private Geometry geometry = null;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
