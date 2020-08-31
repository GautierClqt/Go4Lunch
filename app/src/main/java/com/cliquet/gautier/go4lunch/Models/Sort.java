package com.cliquet.gautier.go4lunch.Models;

import android.content.SharedPreferences;

import com.cliquet.gautier.go4lunch.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;

public class Sort {
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private int mSortType;

    public List<Restaurant> sortList(List<Restaurant> restaurantList, int sortRadioButtonId) {

        mRestaurantList = restaurantList;
        mSortType = sortRadioButtonId;
        checkSortingType();

        return mRestaurantList;
    }

    private void checkSortingType() {
        switch(mSortType) {
            case R.id.activity_setting_sortdefault_radiobutton :
                break;

            case R.id.activity_setting_sortnames_radiobutton: sortByNames();
                break;

            case R.id.activity_setting_sortworkmates_radiobutton: sortByWorkmates();
                break;

            case R.id.activity_setting_sortrating_radiobutton: sortByRating();
                break;

            case R.id.activity_setting_sortproximity_radiobutton: sortByProximity();
                break;
        }
    }

    private void sortByNames() {
        Collections.sort(mRestaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                return restaurant1.getName().compareTo(restaurant2.getName());
            }
        });
    }

    private void sortByWorkmates() {
        Collections.sort(mRestaurantList, new Comparator<Restaurant>() {

            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                return restaurant2.getNumberOfWorkmates() - restaurant1.getNumberOfWorkmates();
            }
        });
    }

    private void sortByRating() {
        Collections.sort(mRestaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                return restaurant2.getRating() - restaurant1.getRating();
            }
        });
    }

    private void sortByProximity() {
        Collections.sort(mRestaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                return Float.compare(restaurant1.getDistance(), restaurant2.getDistance());
            }
        });
    }
}
