package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;

public class RestaurantDetails extends AppCompatActivity {

    Restaurant restaurant;

    TextView name;
    TextView address;
    ImageView firstStar;
    ImageView secondStar;
    ImageView thirdStar;
    ImageView selected;
    Button call;
    Button likeByUser;
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

        firstStar.setVisibility(View.INVISIBLE);
        secondStar.setVisibility(View.INVISIBLE);
        thirdStar.setVisibility(View.INVISIBLE);

        //display stars according to the number of likes
        if(restaurant.getAllLikes() > 0) {
            firstStar.setVisibility(View.VISIBLE);
        }
        if(restaurant.getAllLikes() > 1) {
            secondStar.setVisibility(View.VISIBLE);
        }
        if(restaurant.getAllLikes() > 2) {
            thirdStar.setVisibility(View.VISIBLE);
        }

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

        if(restaurant.getWebsiteUrl() == null || restaurant.getPhone().equals("")) {
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
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +restaurant.getPhone()));
        startActivity(intent);
    }

    private void startWebsiteActivity() {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra("url", restaurant.getWebsiteUrl());
        startActivity(intent);
    }

    private void initViews() {
        name = findViewById(R.id.activity_restaurant_details_name_textview);
        address = findViewById(R.id.activity_restaurant_details_address_textview);
        firstStar = findViewById(R.id.activity_restaurant_details_firststar_imageview);
        secondStar = findViewById(R.id.activity_restaurant_details_secondstar_imageview);
        thirdStar = findViewById(R.id.activity_restaurant_details_thirdstar_imageview);
        selected = findViewById(R.id.activity_restaurant_details_selectrestaurant_button);
        call = findViewById(R.id.activity_restaurant_details_call_button);
        likeByUser = findViewById(R.id.activity_restaurant_details_likebyuser_button);
        website = findViewById(R.id.activity_restaurant_details_website_button);
        picture = findViewById(R.id.activity_restaurant_details_picture_imageview);
        recyclerView = findViewById(R.id.activity_restaurant_details_workmates_recyclerview);
    }
}
