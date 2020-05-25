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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.cliquet.gautier.go4lunch.Utils.AlarmStartStop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements PlacesApiCalls.GoogleMapsCallback, NavigationView.OnNavigationItemSelectedListener {

    private boolean search = false;

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView textViewPermissions;
    TextView userNameTextview;
    TextView userEmailTextview;
    ImageView userPictureImageview;

    private NearbySearchPojo mNearbySearchPojo;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private ArrayList<Restaurant> mRestaurantOriginalList = new ArrayList<>();
    private ArrayList<Workmates> mWorkmatesList = new ArrayList<>();

    private static final Fragment MAP = new MapFragment();
    private static final Fragment LIST = new ListFragment();
    private static final Fragment WORKMATES = new WorkmatesFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment = MAP;

    private HashMap<String, String> mRequestParametersHM = new HashMap<>();
    private double mUserLat;
    private double mUserLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationSwitchPosition();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        }

        permissionsChecking();
        configureDrawerLayout();
        bindViews();
        setNavigationDrawerViews(); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_menu_search_item);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                search = true;
                if(mRestaurantOriginalList.isEmpty()) {
                    mRestaurantOriginalList.addAll(mRestaurantList);
                }

                if(!searchText.equals("")) {
                    mRestaurantList.clear();
                    googleMpaApiSearchRequest(searchText);
                    Log.d("tag", "onQueryTextChange: "+searchText);
                }
                else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mRestaurantList.clear();
                    mRestaurantList.addAll(mRestaurantOriginalList);
                    configureBundle();
                }
                return false;
            }
        });
        return true;
    }

    private void notificationSwitchPosition() {
        SharedPreferences preferences;
        preferences = getSharedPreferences("Go4Lunch_Settings", MODE_PRIVATE);
        boolean switchPosition = preferences.getBoolean("switch_position", true);

        AlarmStartStop alarm = new AlarmStartStop();
        if(switchPosition) {
            alarm.startAlarm(this);
        }
        else {
            alarm.stopAlarm(this);
        }
    }

    private void configureBottomView() {
//        if(activeFragment == LIST) {
//            fragmentManager.beginTransaction().detach(activeFragment).attach(activeFragment).addToBackStack(null).commit();
//        }
        fragmentManager.beginTransaction().detach(LIST).attach(LIST).addToBackStack(null).commit();
        fragmentManager.beginTransaction().detach(MAP).attach(MAP).addToBackStack(null).commit();
        //progressBar.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_navigation_menu_map:
                        fragmentManager.beginTransaction().hide(activeFragment).show(MAP).commit();
                        activeFragment = MAP;
                        return true;

                    case R.id.bottom_navigation_menu_list:
                        fragmentManager.beginTransaction().hide(activeFragment).show(LIST).commit();
                        activeFragment = LIST;
                        return true;

                    case R.id.bottom_navigation_menu_workmates:
                        fragmentManager.beginTransaction().hide(activeFragment).show(WORKMATES).commit();
                        activeFragment = WORKMATES;
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
        firestoreGetUsersRequest();
        getUserLocation();
    }

    private void configureFragmentsDefaultDisplay() {
        fragmentManager.beginTransaction().add(R.id.activity_main_framelayout, WORKMATES, "3").hide(WORKMATES).commit();
        fragmentManager.beginTransaction().add(R.id.activity_main_framelayout, LIST, "2").hide(LIST).commit();
        fragmentManager.beginTransaction().add(R.id.activity_main_framelayout, MAP, "1").commit();
    }

    private void bindViews() {
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        navigationView = findViewById(R.id.avitivity_main_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userNameTextview = headerView.findViewById(R.id.nav_header_username_textview);
        userEmailTextview = headerView.findViewById(R.id.nav_header_useremail_textview);
        userPictureImageview = headerView.findViewById(R.id.nav_header_userpicture_imageview);
        textViewPermissions = findViewById(R.id.activity_main_warning_textview);
    }

    private void setNavigationDrawerViews() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userNameTextview.setText(auth.getCurrentUser().getDisplayName());
        userEmailTextview.setText(auth.getCurrentUser().getEmail());
        Glide.with(userPictureImageview).load(auth.getCurrentUser().getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(userPictureImageview);
    }

    private void configureDrawerLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
        mRequestParametersHM.clear();
        mRequestParametersHM.put("location", mUserLat + "," + mUserLng);
        mRequestParametersHM.put("radius", Integer.toString(500));
        mRequestParametersHM.put("type", "restaurant");
        mRequestParametersHM.put("key", this.getResources().getString(R.string.google_maps_key));

        PlacesApiCalls.fetchNearbySearch(this, mRequestParametersHM);
    }

    private void googleMpaApiSearchRequest(String searchText) {
        mRequestParametersHM.clear();
        mRequestParametersHM.put("input", searchText);
        mRequestParametersHM.put("inputtype", "textquery");
        mRequestParametersHM.put("type", "restaurant");
        mRequestParametersHM.put("key", this.getResources().getString(R.string.google_maps_key));

        PlacesApiCalls.fetchFromText(this, mRequestParametersHM);
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

        MAP.setArguments(bundle);
        LIST.setArguments(bundle);
        WORKMATES.setArguments(bundle);

        if(!search) {
            configureFragmentsDefaultDisplay();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            case R.id.drawer_menu_your_lunch_item:
                UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().get("userSelected")!= null) {
                            String restaurantId = task.getResult().get("userSelected").toString();
                            for(int i = 0; i < mRestaurantList.size(); i++) {
                                if(mRestaurantList.get(i).getId().equals(restaurantId)) {
                                    startActivityRestaurantDetails(mRestaurantList.get(i));
                                }
                            }
                        }
                        else {
                            toastNoRestaurantSelected();
                        }
                    }
                });
                break;

            case R.id.drawer_menu_settings_item:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.drawer_menu_logout_item:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    private void startActivityRestaurantDetails(Restaurant restaurant) {
        if(restaurant != null) {
            Intent restaurantDetailsActivityIntent = new Intent(this, RestaurantDetails.class);
            restaurantDetailsActivityIntent.putExtra("restaurant", restaurant);
            startActivity(restaurantDetailsActivityIntent);
        }

    }

    private void toastNoRestaurantSelected() {
        Toast.makeText(this, "No restaurant selected",Toast.LENGTH_LONG).show();
    }
}
