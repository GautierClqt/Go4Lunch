package com.cliquet.gautier.go4lunch.Models;

import android.location.Location;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BundleDataHandler {

    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private NearbySearchPojo mNearbySearchPojo = new NearbySearchPojo();
    private DetailsPojo mDetailsPojo = new DetailsPojo();
    private double mUserLat;
    private double mUserLng;


    public ArrayList<Restaurant> fillingRestaurantsList(NearbySearchPojo nearbySearchPojo, DetailsPojo detailsPojo, double userLat, double userLng, int index) {
        mNearbySearchPojo = nearbySearchPojo;
        mDetailsPojo = detailsPojo;
        mUserLat = userLat;
        mUserLng = userLng;

        double restaurantLat = 0;
        double restaurantLng = 0;
        float distance = 0;
        Hours hours = new Hours();
        String openingHoursString;
        String phoneNumber;
        String website;
        String photoReference;

        if (detailsPojo.getResults().getPhoneNumber() != null) {
            phoneNumber = detailsPojo.getResults().getPhoneNumber();
        } else {
            phoneNumber = null;
        }

        if (detailsPojo.getResults().getWebsite() != null) {
            website = detailsPojo.getResults().getWebsite();
        } else {
            website = null;
        }

        if (nearbySearchPojo.getNearbySearchResults().get(index).getPhotos() != null) {
            photoReference = nearbySearchPojo.getNearbySearchResults().get(index).getPhotos().get(0).getPhotoReference();
        } else {
            photoReference = null;
        }

        if (detailsPojo.getResults().getOpeningHours() != null) {
            openingHoursString = hours.getOpeningHours(detailsPojo.getResults().getOpeningHours().getOpenNow(), detailsPojo.getResults().getOpeningHours().getPeriods());
        } else {
            openingHoursString = null;
        }

        if(nearbySearchPojo.getNearbySearchResults().get(index).getGeometry() != null) {
            restaurantLat = nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat();
            restaurantLng = nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLng();
            distance = calculateDistance(index);
        }

        mRestaurantList.add(new Restaurant(
                nearbySearchPojo.getNearbySearchResults().get(index).getId(),
                nearbySearchPojo.getNearbySearchResults().get(index).getName(),
                restaurantLat,
                restaurantLng,
                nearbySearchPojo.getNearbySearchResults().get(index).getVicinity(),
                false,
                new Random().nextInt(4),
                false,
                phoneNumber,
                website,
                distance,
                photoReference,
                openingHoursString));

        return mRestaurantList;
    }

    private float calculateDistance(int index){
        Location userLocation = new Location("user location");
        userLocation.setLatitude(mUserLat);
        userLocation.setLongitude(mUserLng);

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat());
        restaurantLocation.setLongitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat());

        return userLocation.distanceTo(restaurantLocation);
    }

}
