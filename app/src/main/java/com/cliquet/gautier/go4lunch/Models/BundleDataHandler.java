package com.cliquet.gautier.go4lunch.Models;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.cliquet.gautier.go4lunch.Controllers.Fragments.BundleCallback;
import com.cliquet.gautier.go4lunch.Controllers.UserLocationCallback;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.PlacesApiCalls;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
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
import java.util.Random;

public class BundleDataHandler implements PlacesApiCalls.GoogleMapsCallback {

    private Context mContext;
    private Bundle mBundle = new Bundle();
    private BundleCallback mBundleCallback;

    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private NearbySearchPojo mNearbySearchPojo = new NearbySearchPojo();
    private ArrayList<Workmates> mWorkmatesList = new ArrayList<>();

    private double mUserLat;
    private double mUserLng;

    public Bundle collectingDatas(Context context) {
        this.mContext = context;

        firestoreGetUsersRequest();
        //locateUser();

        return mBundle;
    }

    public void firestoreGetUsersRequest() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("users");

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);

                    String selectedRestaurant;
                    if (documentSnapshot.get("userSelected") == null) {
                        selectedRestaurant = null;
                    } else {
                        selectedRestaurant = documentSnapshot.get("userSelected").toString();
                    }

                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUserId())) {
                        mWorkmatesList.add(new Workmates(
                                user.getUserId(),
                                user.getUserFirstName(),
                                user.getUserLastName(),
                                user.getUserEmail(),
                                user.getUserUrlPicture(),
                                selectedRestaurant
                        ));
                    }
                }
            }
        });
    }

    public void locateUser(Context context, final UserLocationCallback callback) {
        final double[] userLocation = new double[2];
        FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(context);
        location.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mUserLat = location.getLatitude();
                mUserLng = location.getLongitude();

                userLocation[0] = mUserLat;
                userLocation[1] = mUserLng;
                callback.onCallback(userLocation);
            }
        });
    }
    
    public void googleMapsNearbySearchRequest(String GOOGLE_API_KEY, double userLat, double userLng, BundleCallback callback) {
        this.mBundleCallback = callback;

        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.clear();
        requestParameters.put("location", userLat + "," + userLng);
        requestParameters.put("radius", Integer.toString(500));
        requestParameters.put("type", "restaurant");
        requestParameters.put("key", GOOGLE_API_KEY);

        PlacesApiCalls.fetchNearbySearch(this, requestParameters);
    }

    private void googleMapsDetailsRequest(NearbySearchPojo nearbySearchPojo) {
        for (int i = 0; i <= nearbySearchPojo.getNearbySearchResults().size() - 1; i++) {
            PlacesApiCalls.fetchDetails(this, nearbySearchPojo.getNearbySearchResults().get(i).getId(), i);
        }
    }

    private void googleMpaApiSearchRequest(String GOOGLE_API_KEY, String searchText) {
        HashMap<String, String> requestParameters = new HashMap<>();
        requestParameters.clear();
        requestParameters.put("input", searchText);
        requestParameters.put("inputtype", "textquery");
        requestParameters.put("type", "restaurant");
        requestParameters.put("key", GOOGLE_API_KEY);

        PlacesApiCalls.fetchFromText(this, requestParameters);
    }

    public void fillingRestaurantsList(NearbySearchPojo nearbySearchPojo, DetailsPojo detailsPojo, int index) {
        mNearbySearchPojo = nearbySearchPojo;

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

        if (mRestaurantList.size() == nearbySearchPojo.getNearbySearchResults().size()) {
            configureBundle();
        }
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

    private void configureBundle() {
        Gson gson = new Gson();
        String gsonRestaurantsList;
        String gsonWorkmatesList;

        gsonRestaurantsList = gson.toJson(mRestaurantList);
        gsonWorkmatesList = gson.toJson(mWorkmatesList);

        mBundle.putString("restaurant_list", gsonRestaurantsList);
        mBundle.putString("workmates_list", gsonWorkmatesList);

        mBundleCallback.onCallback(mBundle);
    }

    @Override
    public void onResponse(NearbySearchPojo nearbySearchPojo) {

        mNearbySearchPojo = nearbySearchPojo;

        if (mNearbySearchPojo.getNearbySearchResults().size() != 0) {
            mNearbySearchPojo.setNearbySearchResults(mNearbySearchPojo.getNearbySearchResults());
            googleMapsDetailsRequest(mNearbySearchPojo);
        } else {
//            textViewPermissions.setText(R.string.no_restaurant_found);
//            textViewPermissions.setVisibility(View.VISIBLE);
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
