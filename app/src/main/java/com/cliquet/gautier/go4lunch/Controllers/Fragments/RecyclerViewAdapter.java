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
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchResults;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<NearbySearchResults> mNearbySearch;
    private Restaurant mRestaurant;

    private String stringRestaurant;

    public RecyclerViewAdapter(Context context, List<NearbySearchResults> mNearbySearch) {
        this.mContext = context;
        this.mNearbySearch = mNearbySearch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_restaurant, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.name.setText(mNearbySearch.get(i).getName());
        viewHolder.adress.setText(mNearbySearch.get(i).getVicinity());
        if(mNearbySearch.get(i).getOpeningHours() != null) {
            if(!mNearbySearch.get(i).getOpeningHours().getOpenNow()) {
                viewHolder.hours.setText(mContext.getString(R.string.restaurant_hours_isopen));
                viewHolder.hours.setTextColor(Color.parseColor("#00cc00"));
            }
            else {
                viewHolder.hours.setText(mContext.getString(R.string.restaurant_hours_notopen));
                viewHolder.hours.setTextColor(Color.parseColor("#ff0000"));
            }
        }
        viewHolder.distance.setText("160");
        viewHolder.workmatesCount.setText("(2)");
        viewHolder.workmatesIcon.setImageResource(R.drawable.recyclerview_workmatesicon);

        final String photoReference = mNearbySearch.get(i).getPhotos().get(0).getPhotoReference();
        String apiKey = mContext.getString(R.string.google_maps_key);
        Glide.with(viewHolder.picture).load("https://maps.googleapis.com/maps/api/place/photo?key="+apiKey+"&photoreference="+photoReference+"&maxwidth=600").into(viewHolder.picture);

        mRestaurant = new Restaurant("Le Restaurant("+i+")", "5 rue de la Sirène", false, false, "00.00.78.98.52", "https://www.restaurant.fr", photoReference);

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mRestaurant = new Restaurant("Le Restaurant", "5 rue de la Sirène", false, false, "06.07.78.98.52", "https://www.restaurant.fr", photoReference);

                Gson gson = new Gson();
                stringRestaurant = gson.toJson(mRestaurant);

                Intent restaurantDetailsActivityIntent = new Intent(viewHolder.mainLayout.getContext(), RestaurantDetails.class);
                restaurantDetailsActivityIntent.putExtra("restaurant", stringRestaurant);

                mContext.startActivity(restaurantDetailsActivityIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mNearbySearch.size();
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
            adress = itemView.findViewById(R.id.item_recycler_restaurant_adress_textview);
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
