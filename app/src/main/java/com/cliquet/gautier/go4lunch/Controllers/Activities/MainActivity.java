package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cliquet.gautier.go4lunch.Controllers.Fragments.ListFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.MapFragment;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView textViewPermissions;

    final Fragment mapFragment = new MapFragment();
    final Fragment listFragment = new ListFragment();
    final Fragment workmatesFragment = new WorkmatesFragment();
    final FragmentManager fragmentMangager = getSupportFragmentManager();
    Fragment activeFragment = mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        permissionsChecking();
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
        configureFragmentsDefaultDisplay();
        configureBottomView();
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
}
