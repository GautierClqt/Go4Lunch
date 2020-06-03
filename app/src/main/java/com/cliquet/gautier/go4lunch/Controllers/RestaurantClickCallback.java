package com.cliquet.gautier.go4lunch.Controllers;

import com.cliquet.gautier.go4lunch.Models.Restaurant;

public interface RestaurantClickCallback {
    void onItemClicked(int position, Restaurant restaurant);
}
