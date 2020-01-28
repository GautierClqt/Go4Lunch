package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Models.Pojo.GoogleMapsPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestaurantDetails extends AppCompatActivity {

    Restaurant restaurant;
    Gson gson = new Gson();

    TextView name;
    TextView address;
    ImageView starLiked;
    ImageView selected;
    ImageView iconPhone;
    ImageView iconLike;
    ImageView iconWebsite;
    ImageView picture;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        initViews();

        String stringRestaurant = getIntent().getStringExtra("restaurant");
        restaurant = gson.fromJson(stringRestaurant, new TypeToken<Restaurant>(){}.getType());

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());

        String apiKey = getString(R.string.google_maps_key);
        Glide.with(picture).load("https://maps.googleapis.com/maps/api/place/photo?key=" + apiKey + "&photoreference=" + restaurant.getPhotoReference() + "&maxwidth=600").into(picture);
    }

    private void initViews() {
        name = findViewById(R.id.activity_restaurant_details_name_textview);
        address = findViewById(R.id.activity_restaurant_details_address_textview);
        starLiked = findViewById(R.id.activity_restaurant_details_star_imageview);
        selected = findViewById(R.id.activity_restaurant_details_isselected_imageview);
        iconPhone = findViewById(R.id.activity_restaurant_details_phone_imageview);
        iconLike = findViewById(R.id.activity_restaurant_details_like_imageview);
        iconWebsite = findViewById(R.id.activity_restaurant_details_website_imageview);
        picture = findViewById(R.id.activity_restaurant_details_picture_imageview);
        recyclerView = findViewById(R.id.activity_restaurant_details_workmates_recyclerview);
    }
}