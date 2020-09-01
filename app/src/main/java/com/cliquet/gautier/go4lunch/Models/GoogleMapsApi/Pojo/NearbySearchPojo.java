package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbySearchPojo {
    @SerializedName("results")
    @Expose
    private List<NearbySearchResults> nearbySearchResults;

    public List<NearbySearchResults> getNearbySearchResults() {
        return nearbySearchResults;
    }
    public void setNearbySearchResults(List<NearbySearchResults> nearbySearchResults) {
        this.nearbySearchResults = nearbySearchResults;
    }

    public class NearbySearchResults {
        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        @SerializedName("place_id")
        @Expose
        private String placeId;

        public String getId() {
            return placeId;
        }
        public void setId(String placeId) { this.placeId = placeId; }

        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        public String getVicinity() { return vicinity; }

        @SerializedName("geometry")
        @Expose
        private Geometry geometry = null;

        public Geometry getGeometry() {
            return geometry;
        }

        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours = null;

        public OpeningHours getOpeningHours() { return openingHours; }

        @SerializedName("photos")
        @Expose
        private List<Photos> photos;

        public List<Photos> getPhotos() {
            return photos;
        }


        @SerializedName("rating")
        @Expose
        private float rating;

        public float getRating() { return rating; }
    }

    public class Geometry {
        @SerializedName("location")
        @Expose
        private Location location;

        public Location getLocation() {
            return location;
        }
    }

    public class Location {
        @SerializedName("lat")
        @Expose
        private double lat;

        public double getLat() {
            return lat;
        }

        @SerializedName("lng")
        @Expose
        private double lng;

        public double getLng() {
            return lng;
        }
    }

    public class Photos {
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;

        public String getPhotoReference() {
            return photoReference;
        }

    }
}
