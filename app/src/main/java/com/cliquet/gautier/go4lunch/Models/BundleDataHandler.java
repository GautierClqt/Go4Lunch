package com.cliquet.gautier.go4lunch.Models;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.cliquet.gautier.go4lunch.Controllers.BundleCallback;
import com.cliquet.gautier.go4lunch.Controllers.UserLocationCallback;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.PlacesApiCalls;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BundleDataHandler implements PlacesApiCalls.GoogleMapsCallback {

    private Context mContext;
    private Bundle mBundle = new Bundle();
    private BundleCallback mBundleCallback;
    private Gson gson = new Gson();

    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private NearbySearchPojo mNearbySearchPojo = new NearbySearchPojo();
    private ArrayList<Workmates> mWorkmatesList = new ArrayList<>();

    private double mUserLat;
    private double mUserLng;

    private boolean mRestaurantListOk = false;
    private boolean mWorkmatesListOk = false;

    public void getFirestoreAndGoogleMapsApiDatas(Context context, BundleCallback bundleCallback) {
        this.mContext = context;
        this.mBundleCallback = bundleCallback;

        getWorkmatesDatas();
    }

    private void configureBundle() {
        if(mWorkmatesListOk && mRestaurantListOk) {

            if(mRestaurantList.isEmpty()) {
                mBundle.putString("restaurant_list", null);
            } else {
                mBundle.putString("restaurant_list", gson.toJson(mRestaurantList));
            }

            if(mWorkmatesList.isEmpty()) {
                mBundle.putString("workmates_list", null);
            } else {
                mBundle.putString("workmates_list", gson.toJson(mWorkmatesList));
            }

            mBundleCallback.onCallback(mBundle);
        }
    }

    private void getWorkmatesDatas() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("users");

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Workmates workmates = documentSnapshot.toObject(Workmates.class);

                    if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(workmates.getId())) {
                        mWorkmatesList.add(new Workmates(
                                workmates.getId(),
                                workmates.getFirstName(),
                                workmates.getLastName(),
                                workmates.getEmail(),
                                workmates.getUrlPicture(),
                                workmates.getSelectedRestaurant()
                        ));
                    }
                }
                mWorkmatesListOk = true;
                getRestaurantsDatas();
            }
        });
    }

    private void getRestaurantsDatas() {
        locateUser(mContext, new UserLocationCallback() {
            @Override
            public void onCallback(double[] userLocation) {
                googleMapsNearbySearchRequest(mContext.getResources().getString(R.string.google_maps_key), userLocation[0], userLocation[1]);
            }
        });
    }

    private void locateUser(Context context, final UserLocationCallback callback) {
        final double[] userLocation = new double[2];
        FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(context);
        location.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    mUserLat = location.getLatitude();
                    mUserLng = location.getLongitude();

                    userLocation[0] = mUserLat;
                    userLocation[1] = mUserLng;
                    mBundle.putString("user_location", gson.toJson(userLocation));

                    callback.onCallback(userLocation);
                } else {
                    mRestaurantListOk = true;
                    mBundle.putString("user_location", null);
                    configureBundle();
                }
            }
        });
    }
    
    private void googleMapsNearbySearchRequest(String GOOGLE_API_KEY, double userLat, double userLng) {
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.clear();
        requestParameters.put("location", userLat + "," + userLng);
        requestParameters.put("radius", Integer.toString(500));
        requestParameters.put("type", "restaurant");
        requestParameters.put("key", GOOGLE_API_KEY);

        PlacesApiCalls.fetchNearbySearch(this, requestParameters);
    }

    public void googleMapsApiSearchRequest(String GOOGLE_API_KEY, String searchText) {
        mRestaurantListOk = false;

        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.clear();
        requestParameters.put("input", searchText);
        requestParameters.put("inputtype", "textquery");
        requestParameters.put("type", "restaurant");
        requestParameters.put("key", GOOGLE_API_KEY);

        PlacesApiCalls.fetchFromText(this, requestParameters);
    }

    private void googleMapsDetailsRequest(NearbySearchPojo nearbySearchPojo) {
        for (int i = 0; i <= nearbySearchPojo.getNearbySearchResults().size() - 1; i++) {
            PlacesApiCalls.fetchDetails(this, nearbySearchPojo.getNearbySearchResults().get(i).getId(), i);
        }
    }

    private void fillingRestaurantsList(NearbySearchPojo nearbySearchPojo, DetailsPojo detailsPojo, int index) {
        mNearbySearchPojo = nearbySearchPojo;

        double restaurantLat = 0;
        double restaurantLng = 0;
        String adress;
        float distance = 0;
        Hours hours = new Hours();
        int rating;
        String openingHoursString;
        String phoneNumber;
        String website;
        String photoReference;

        if(nearbySearchPojo.getNearbySearchResults().get(index).getVicinity() != null) {
            adress = nearbySearchPojo.getNearbySearchResults().get(index).getVicinity();
        }
        else {
            adress = detailsPojo.getResults().getFormattedAdress();
        }

        if (detailsPojo.getResults().getPhoneNumber() != null) {
            phoneNumber = detailsPojo.getResults().getPhoneNumber();
        } else {
            phoneNumber = null;
        }

        if(nearbySearchPojo.getNearbySearchResults().get(index).getRating() > 4) {
            rating = 3;
        }
        else if(nearbySearchPojo.getNearbySearchResults().get(index).getRating() > 2) {
            rating = 2;
        }
        else {
            rating = 1;
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
            openingHoursString = "";
        }

        if(nearbySearchPojo.getNearbySearchResults().get(index).getGeometry() != null) {
            restaurantLat = nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat();
            restaurantLng = nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLng();
            distance = calculateDistance(index);
        }

        int numberOfWorkmates = 0;
        for(int i = 0; i < mWorkmatesList.size(); i++) {
            if(mWorkmatesList.get(i).getSelectedRestaurant() != null) {
                if (mWorkmatesList.get(i).getSelectedRestaurant().equals(nearbySearchPojo.getNearbySearchResults().get(index).getId())) {
                    numberOfWorkmates++;
                }
            }
        }

        mRestaurantList.add(new Restaurant(
                nearbySearchPojo.getNearbySearchResults().get(index).getId(),
                nearbySearchPojo.getNearbySearchResults().get(index).getName(),
                restaurantLat,
                restaurantLng,
                adress,
                numberOfWorkmates,
                false,
                rating,
                false,
                phoneNumber,
                website,
                distance,
                photoReference,
                openingHoursString));

        if (mRestaurantList.size() == nearbySearchPojo.getNearbySearchResults().size()) {
            mRestaurantListOk = true;
            configureBundle();
        }
    }

    private float calculateDistance(int index){
        Location userLocation = new Location("user location");
        userLocation.setLatitude(mUserLat);
        userLocation.setLongitude(mUserLng);

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat());
        restaurantLocation.setLongitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLng());

        return userLocation.distanceTo(restaurantLocation);
    }

    @Override
    public void onResponse(NearbySearchPojo nearbySearchPojo) {
        mNearbySearchPojo = nearbySearchPojo;

        mRestaurantList.clear();

        if (mNearbySearchPojo.getNearbySearchResults().size() != 0) {
            mNearbySearchPojo.setNearbySearchResults(mNearbySearchPojo.getNearbySearchResults());
            googleMapsDetailsRequest(mNearbySearchPojo);
        } else {
            mRestaurantListOk = true;
            configureBundle();
        }
    }

    @Override
    public void onResponse(DetailsPojo detailsPojo, int index) {
        fillingRestaurantsList(mNearbySearchPojo, detailsPojo, index);
    }

    @Override
    public void onFailure() {
    }

}
