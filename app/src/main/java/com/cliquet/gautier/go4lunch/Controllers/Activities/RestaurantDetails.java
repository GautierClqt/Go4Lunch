package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestaurantDetails extends AppCompatActivity {

    Restaurant restaurant;

    TextView name;
    TextView address;
    ImageView starLiked;
    ImageView selected;
    Button call;
    Button like;
    Button website;
    ImageView picture;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        initViews();

        restaurant = getIntent().getParcelableExtra("restaurant");

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());

        String apiKey = getString(R.string.google_maps_key);
        Glide.with(picture).load("https://maps.googleapis.com/maps/api/place/photo?key=" + apiKey + "&photoreference=" + restaurant.getPhotoReference() + "&maxwidth=600").into(picture);

        if(restaurant.getPhone() == null || restaurant.getPhone().equals("")) {
            call.setEnabled(false);
        }
        else {
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCallActivity();
                }
            });
        }

        if(restaurant.getUrl() == null || restaurant.getPhone().equals("")) {
            website.setEnabled(false);
        }
        else {
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWebsiteActivity();
                }
            });
        }
    }

    private void startCallActivity() {
        if (ContextCompat.checkSelfPermission(RestaurantDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RestaurantDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +restaurant.getPhone()));
            startActivity(intent);
        }
    }

    private void startWebsiteActivity() {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra("url", restaurant.getUrl());
        startActivity(intent);
    }

    private void initViews() {
        name = findViewById(R.id.activity_restaurant_details_name_textview);
        address = findViewById(R.id.activity_restaurant_details_address_textview);
        starLiked = findViewById(R.id.activity_restaurant_details_firststar_imageview);
        selected = findViewById(R.id.activity_restaurant_selectrestaurant_button);
        call = findViewById(R.id.activity_restaurant_call_button);
        like = findViewById(R.id.activity_restaurant_like_button);
        website = findViewById(R.id.activity_restaurant_website_button);
        picture = findViewById(R.id.activity_restaurant_details_picture_imageview);
        recyclerView = findViewById(R.id.activity_restaurant_details_workmates_recyclerview);
    }
}
