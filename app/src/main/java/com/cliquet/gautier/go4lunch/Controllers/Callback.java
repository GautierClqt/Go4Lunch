package com.cliquet.gautier.go4lunch.Controllers;

import com.cliquet.gautier.go4lunch.Models.Restaurant;

public interface Callback {
    void onItemClicked(int position, Restaurant restaurant);
}
