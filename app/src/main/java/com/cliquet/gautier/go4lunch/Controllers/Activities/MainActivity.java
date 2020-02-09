package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cliquet.gautier.go4lunch.Controllers.Fragments.ListFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.MapFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.PlacesApiCalls;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PlacesApiCalls.GoogleMapsCallback {

    BottomNavigationView bottomNavigationView;
    TextView textViewPermissions;

    private Restaurant mRestaurant;
    private NearbySearchPojo mNearbySearchPojo;
    private DetailsPojo mDetailsPojo;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private int mCurrentRequest;

    final Fragment mapFragment = new MapFragment();
    final Fragment listFragment = new ListFragment();
    final Fragment workmatesFragment = new WorkmatesFragment();
    final FragmentManager fragmentMangager = getSupportFragmentManager();
    Fragment activeFragment = mapFragment;

    private HashMap<String, String> mRequestParametersHM = new HashMap<>();
    private double mUserLat;
    private double mUserLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        permissionsChecking();
        getUserLocation();
    }

    private void configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.bottom_navigation_menu_map:
                        fragmentMangager.beginTransaction().hide(activeFragment).show(mapFragment).commit();
                        activeFragment = mapFragment;
                        return true;

                    case R.id.bottom_navigation_menu_list:
                        fragmentMangager.beginTransaction().hide(activeFragment).show(listFragment).commit();
                        activeFragment = listFragment;
                        return true;

                    case R.id.bottom_navigation_menu_workmates:
                        fragmentMangager.beginTransaction().hide(activeFragment).show(workmatesFragment).commit();
                        activeFragment = workmatesFragment;
                        return true;
                }
                return false;
            }
        });
    }

    private void permissionsChecking() {
        //will call onRequestPermissionsResult next to handle the response
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private void permissionsNotGranted() {
        textViewPermissions.setVisibility(View.VISIBLE);
    }

    private void permissionsGranted() {
        textViewPermissions.setVisibility(View.GONE);
    }

    private void configureFragmentsDefaultDisplay() {
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, workmatesFragment, "3").hide(workmatesFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, listFragment, "2").hide(listFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, mapFragment, "1").commit();
    }

    private void bindViews() {
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        textViewPermissions = findViewById(R.id.activity_main_textview_permissions);
    }

    private void getUserLocation() {
        FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this));
        location.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mUserLat = location.getLatitude();
                mUserLng = location.getLongitude();
                googleMapApiRequest();
            }
        });
    }

    private void googleMapApiRequest() {
        mRequestParametersHM.put("location", mUserLat+","+mUserLng);
        mRequestParametersHM.put("radius", Integer.toString(500));
        mRequestParametersHM.put("type", "restaurant");
        mRequestParametersHM.put("key", this.getResources().getString(R.string.google_maps_key));

        PlacesApiCalls.fetchNearbySearch(this, mRequestParametersHM);
    }

    private void detailsRequest(NearbySearchPojo nearbySearchPojo) {
        for(int i=0; i <= nearbySearchPojo.getNearbySearchResults().size()-1; i++) {
            mCurrentRequest = i;
            PlacesApiCalls.fetchDetails(this, nearbySearchPojo.getNearbySearchResults().get(i).getId());
        }
    }

    private void fillingRestaurantsList(NearbySearchPojo nearbySearchPojo, DetailsPojo detailsPojo) {
        mRestaurantList.add(new Restaurant(
                nearbySearchPojo.getNearbySearchResults().get(mCurrentRequest).getName(),
                nearbySearchPojo.getNearbySearchResults().get(mCurrentRequest).getVicinity(),
                false,
                false,
                detailsPojo.getResults().getPhoneNumber(),
                detailsPojo.getResults().getWebsite(),
                nearbySearchPojo.getNearbySearchResults().get(mCurrentRequest).getPhotos().get(0).getPhotoReference(),
                detailsPojo.getResults().getOpeningHours().getPeriods()));
    }

    private void configureBundle(List<Restaurant> restaurantList) {
        restaurantList = mRestaurantList;
        Gson gson = new Gson();
        String gsonRestaurantList;

        gsonRestaurantList = gson.toJson(restaurantList);

        Bundle bundle = new Bundle();
        bundle.putString("restaurant_list", gsonRestaurantList);

        mapFragment.setArguments(bundle);
        listFragment.setArguments(bundle);

        configureFragmentsDefaultDisplay();
        configureBottomView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted();
        }
        else {
            permissionsNotGranted();
        }
    }

    @Override
    public void onResponse(NearbySearchPojo nearbySearchPojo) {

        mNearbySearchPojo = nearbySearchPojo;

        if(mNearbySearchPojo.getNearbySearchResults().size() != 0) {
            mNearbySearchPojo.setNearbySearchResults(mNearbySearchPojo.getNearbySearchResults());
            detailsRequest(mNearbySearchPojo);
        }
        configureBundle(mRestaurantList);
    }

    @Override
    public void onResponse(DetailsPojo detailsPojo) {
        mDetailsPojo = detailsPojo;
        fillingRestaurantsList(mNearbySearchPojo, mDetailsPojo);
    }

    @Override
    public void onFailure() {

    }
}
