package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo;


import com.cliquet.gautier.go4lunch.Models.OpeningHours;
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


    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    public String getNextPageToken() { return nextPageToken; }
    public void setNextPageToken(String nextPageToken) { this.nextPageToken = nextPageToken; }

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

        @SerializedName("photos")
        @Expose
        private List<Photos> photos;

        public List<Photos> getPhotos() {
            return photos;
        }
        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }
    }

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

    public class Location {
        @SerializedName("lat")
        @Expose
        private double lat;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        @SerializedName("lng")
        @Expose
        private double lng;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public class Photos {
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }
    }
}
