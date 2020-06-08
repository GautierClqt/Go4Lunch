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
import com.cliquet.gautier.go4lunch.Controllers.Fragments.BundleCallback;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.ListFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.MapFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.cliquet.gautier.go4lunch.Controllers.UserLocationCallback;
import com.cliquet.gautier.go4lunch.Models.BundleDataHandler;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.cliquet.gautier.go4lunch.Utils.AlarmStartStop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.CallbackHandler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final BundleDataHandler mBundleDataHandler = new BundleDataHandler();

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

    private final Fragment MAP = new MapFragment();
    private final Fragment LIST = new ListFragment();
    private final Fragment WORKMATES = new WorkmatesFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment = MAP;

    private HashMap<String, String> mRequestParametersHM = new HashMap<>();

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
//                    mRestaurantList.clear();
//                    mBundleDataHandler.googleMapsApiSearchRequest(getString(R.string.google_api_key), searchText, new BundleCallback() {
//                                @Override
//                                public void onCallback(Bundle bundle) {
//                                    MAP.setArguments(bundle);
//                                    LIST.setArguments(bundle);
//
//                                    configureBottomView();
//                                }
//                            });
//                            Log.d("tag", "onQueryTextChange: " + searchText);
                }
                else {
                    mRestaurantList.clear();
//                    mRestaurantList.addAll(mRestaurantOriginalList);
//                    configureBundle();
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
        fragmentManager.beginTransaction().detach(LIST).attach(LIST).addToBackStack(null).commit();
        fragmentManager.beginTransaction().detach(MAP).attach(MAP).addToBackStack(null).commit();
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

        //workmatesList
        mBundleDataHandler.getFirestoreAndGoogleMapsApiDatas(this, new BundleCallback() {
                    @Override
                    public void onCallback(Bundle bundle) {
                        MAP.setArguments(bundle);
                        LIST.setArguments(bundle);
                        WORKMATES.setArguments(bundle);

                        configureFragmentsDefaultDisplay();
                        configureBottomView();
                    }
        });
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
