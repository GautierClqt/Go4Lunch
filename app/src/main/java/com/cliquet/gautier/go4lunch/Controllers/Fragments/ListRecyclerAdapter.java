package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Controllers.RestaurantClickCallback;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;

import java.util.List;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private RestaurantClickCallback listener;
    private List<Restaurant> mRestaurantsList;

    ListRecyclerAdapter(Context context, List<Restaurant> restaurantList, RestaurantClickCallback listener) {
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

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.firstStar.setVisibility(View.INVISIBLE);
        viewHolder.secondStar.setVisibility(View.INVISIBLE);
        viewHolder.thirdStar.setVisibility(View.INVISIBLE);

        //display stars according to the number of likes
        if(mRestaurantsList.get(i).getRating() > 0) {
            viewHolder.firstStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurantsList.get(i).getRating() > 1) {
            viewHolder.secondStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurantsList.get(i).getRating() > 2) {
            viewHolder.thirdStar.setVisibility(View.VISIBLE);
        }

        if(mRestaurantsList.get(i).getNumberOfWorkmates() > 0) {
            viewHolder.workmatesIcon.setVisibility(View.VISIBLE);
            viewHolder.workmatesCount.setVisibility(View.VISIBLE);
            viewHolder.workmatesCount.setText(String.valueOf(mRestaurantsList.get(i).getNumberOfWorkmates()));
        }
        else {
            viewHolder.workmatesIcon.setVisibility(View.INVISIBLE);
            viewHolder.workmatesCount.setVisibility(View.INVISIBLE);
        }

        viewHolder.name.setText(mRestaurantsList.get(i).getName());
        viewHolder.name.setTypeface(Typeface.DEFAULT_BOLD);

        viewHolder.adress.setText(mRestaurantsList.get(i).getAdress());
        viewHolder.hours.setText(mRestaurantsList.get(i).getOpeningHours());

        if(mRestaurantsList.get(i).getDistance() < 3000) {
            viewHolder.distance.setText(String.format("%.0fm", mRestaurantsList.get(i).getDistance()));
        }
        else {
            viewHolder.distance.setText(R.string.long_distance);
        }

        final String photoReference = mRestaurantsList.get(i).getPhotoReference();
        if(photoReference != null) {
            String apiKey = mContext.getString(R.string.google_maps_key);
            Glide.with(viewHolder.picture).load("https://maps.googleapis.com/maps/api/place/photo?key=" + apiKey + "&photoreference=" + photoReference + "&maxwidth=600").into(viewHolder.picture);
        }

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
