package com.example.go4lunch.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.go4lunch.Fragments.ListFragment;
import com.example.go4lunch.Fragments.MapFragment;
import com.example.go4lunch.Fragments.WorkmatesFragment;
import com.example.go4lunch.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.activity_main_bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    final Fragment mapFragment = new MapFragment();
    final Fragment listFragment = new ListFragment();
    final Fragment workmatesFragment = new WorkmatesFragment();
    final FragmentManager fragmentMangager = getSupportFragmentManager();
    Fragment activeFragment = mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);

        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, workmatesFragment, "3").hide(workmatesFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, listFragment, "2").hide(listFragment).commit();
        fragmentMangager.beginTransaction().add(R.id.activity_main_framelayout, mapFragment, "1").commit();

        this.configureBottomView();
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

    private Boolean updateMainFragement(Integer integer){
        switch(integer) {
            case R.id.bottom_navigation_menu_map:

        }

            return true;
    }

}
