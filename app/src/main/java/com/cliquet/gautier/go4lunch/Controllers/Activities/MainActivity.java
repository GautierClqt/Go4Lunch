package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.ListFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.MapFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.PlacesApiCalls;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Hours;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.User;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements PlacesApiCalls.GoogleMapsCallback {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    TextView textViewPermissions;
    private ProgressBar progressBar;

    private NearbySearchPojo mNearbySearchPojo;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private ArrayList<Workmates> mWorkmatesList = new ArrayList<>();

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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        permissionsChecking();
        bindViews();
    }

    private void configureBottomView() {
        //progressBar.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
        progressBar.setVisibility(View.VISIBLE);
        firestoreGetUsersRequest();
        getUserLocation();
    }

    private void configureFragmentsDefaultDisplay() {
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, workmatesFragment, "3").hide(workmatesFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, listFragment, "2").hide(listFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, mapFragment, "1").commit();
    }

    private void bindViews() {
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.setDrawerListener(drawerToggle);
        textViewPermissions = findViewById(R.id.activity_main_warning_textview);
        progressBar = findViewById(R.id.activity_main_progressbar);
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
        mRequestParametersHM.put("location", mUserLat + "," + mUserLng);
        mRequestParametersHM.put("radius", Integer.toString(500));
        mRequestParametersHM.put("type", "restaurant");
        mRequestParametersHM.put("key", this.getResources().getString(R.string.google_maps_key));

        PlacesApiCalls.fetchNearbySearch(this, mRequestParametersHM);
    }

    private void firestoreGetUsersRequest() {
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

    private void detailsRequest(NearbySearchPojo nearbySearchPojo) {
        for (int i = 0; i <= nearbySearchPojo.getNearbySearchResults().size() - 1; i++) {
            PlacesApiCalls.fetchDetails(this, nearbySearchPojo.getNearbySearchResults().get(i).getId(), i);
        }
    }

    private void fillingRestaurantsList(NearbySearchPojo nearbySearchPojo, DetailsPojo detailsPojo, int index) {
        float distance = calculateDistance(index);
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

        mRestaurantList.add(new Restaurant(
                nearbySearchPojo.getNearbySearchResults().get(index).getId(),
                nearbySearchPojo.getNearbySearchResults().get(index).getName(),
                nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat(),
                nearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLng(),
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

    private float calculateDistance(int index) {
        Location userLocation = new Location("user location");
        userLocation.setLatitude(mUserLat);
        userLocation.setLongitude(mUserLng);

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLat());
        restaurantLocation.setLongitude(mNearbySearchPojo.getNearbySearchResults().get(index).getGeometry().getLocation().getLng());

        return userLocation.distanceTo(restaurantLocation);
    }

    private void configureBundle() {
        Gson gson = new Gson();
        String gsonRestaurantsList;
        String gsonWorkmatesList;

        gsonRestaurantsList = gson.toJson(mRestaurantList);
        gsonWorkmatesList = gson.toJson(mWorkmatesList);

        Bundle bundle = new Bundle();
        bundle.putString("restaurant_list", gsonRestaurantsList);
        bundle.putString("workmates_list", gsonWorkmatesList);

        mapFragment.setArguments(bundle);
        listFragment.setArguments(bundle);
        workmatesFragment.setArguments(bundle);

        configureFragmentsDefaultDisplay();
        configureBottomView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted();
        } else {
            permissionsNotGranted();
        }
    }

    @Override
    public void onResponse(NearbySearchPojo nearbySearchPojo) {

        mNearbySearchPojo = nearbySearchPojo;

        if (mNearbySearchPojo.getNearbySearchResults().size() != 0) {
            mNearbySearchPojo.setNearbySearchResults(mNearbySearchPojo.getNearbySearchResults());
            detailsRequest(mNearbySearchPojo);
        } else {
            textViewPermissions.setText(R.string.no_restaurant_found);
            textViewPermissions.setVisibility(View.VISIBLE);
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
