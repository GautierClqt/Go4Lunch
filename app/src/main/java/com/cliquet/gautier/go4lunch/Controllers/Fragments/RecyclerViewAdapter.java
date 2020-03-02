package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Controllers.Callback;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.libraries.places.api.model.DayOfWeek;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Callback listener;

    private Context mContext;
    private List<Restaurant> mRestaurantsList;

    public RecyclerViewAdapter(Context context, List<Restaurant> restaurantList, Callback listener) {
        this.mContext = context;
        this.mRestaurantsList = restaurantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_restaurant, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.firstStar.setVisibility(View.INVISIBLE);
        viewHolder.secondStar.setVisibility(View.INVISIBLE);
        viewHolder.thirdStar.setVisibility(View.INVISIBLE);

        //display stars according to the number of likes
        if(mRestaurantsList.get(i).getAllLikes() > 0) {
            viewHolder.firstStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurantsList.get(i).getAllLikes() > 1) {
            viewHolder.secondStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurantsList.get(i).getAllLikes() > 2) {
            viewHolder.thirdStar.setVisibility(View.VISIBLE);
        }

        viewHolder.name.setText(mRestaurantsList.get(i).getName());
        viewHolder.adress.setText(mRestaurantsList.get(i).getAddress());
        viewHolder.hours.setText(mRestaurantsList.get(i).getOpeningHoursString());
        viewHolder.distance.setText(String.format("%.0fm", mRestaurantsList.get(i).getDistance()));
        viewHolder.workmatesCount.setText("(2)");
        viewHolder.workmatesIcon.setImageResource(R.drawable.recyclerview_workmatesicon);

        final String photoReference = mRestaurantsList.get(i).getPhotoReference();
        String apiKey = mContext.getString(R.string.google_maps_key);
        Glide.with(viewHolder.picture).load("https://maps.googleapis.com/maps/api/place/photo?key="+apiKey+"&photoreference="+photoReference+"&maxwidth=600").into(viewHolder.picture);

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(viewHolder.getAdapterPosition(), mRestaurantsList.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mainLayout;
        TextView name;
        TextView adress;
        TextView hours;
        TextView distance;
        TextView workmatesCount;
        ImageView workmatesIcon;
        ImageView firstStar;
        ImageView secondStar;
        ImageView thirdStar;
        ImageView picture;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.item_recycler_restaurant_constraintlayout_main);
            name = itemView.findViewById(R.id.item_recycler_restaurant_name_textview);
            adress = itemView.findViewById(R.id.item_recycler_restaurant_address_textview);
            hours = itemView.findViewById(R.id.item_recycler_restaurant_hours_textview);
            distance = itemView.findViewById(R.id.item_recycler_restaurant_distance_textview);
            workmatesCount = itemView.findViewById(R.id.item_recycler_restaurant_workmates_textview);
            workmatesIcon = itemView.findViewById(R.id.item_recycler_restaurant_workmates_imageview);
            firstStar = itemView.findViewById(R.id.item_recycler_restaurant_firststar_imageview);
            secondStar = itemView.findViewById(R.id.item_recycler_restaurant_secondstar_imageview);
            thirdStar = itemView.findViewById(R.id.item_recycler_restaurant_thirdstar_imageview);
            picture = itemView.findViewById(R.id.item_recycler_restaurant_picture_imageview);
        }
    }
}
