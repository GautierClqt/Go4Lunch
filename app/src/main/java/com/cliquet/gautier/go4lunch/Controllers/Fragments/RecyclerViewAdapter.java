package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Models.Pojo.Results;
import com.cliquet.gautier.go4lunch.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Results> mResults;

    public RecyclerViewAdapter(Context context, List<Results> mResults) {
        this.mContext = context;
        this.mResults = mResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_restaurant, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(mResults.get(i).getName());
        viewHolder.adress.setText(mResults.get(i).getVicinity());
        if(mResults.get(i).getOpeningHours() != null) {
            viewHolder.hours.setText(mResults.get(i).getOpeningHours().getOpenNow());
        }
        viewHolder.distance.setText("160");
        //viewHolder.workmatesCount.setText("(2)");
        //viewHolder.stars...
        //viewHolder.picture...
        String photoReference = mResults.get(i).getPhotos().get(0).getPhotoReference();
        String apiKey = mContext.getString(R.string.google_maps_key);
        Glide.with(viewHolder.picture).load("https://maps.googleapis.com/maps/api/place/photo?key="+apiKey+"&photoreference="+photoReference+"&maxwidth=600").into(viewHolder.picture);
    }


    @Override
    public int getItemCount() {
        return mResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView adress;
        TextView hours;
        TextView distance;
        TextView workmatesCount;
        ImageView workmatesIcon;
        ImageView stars;
        ImageView picture;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_recycler_restaurant_name_textview);
            adress = itemView.findViewById(R.id.item_recycler_restaurant_adress_textview);
            hours = itemView.findViewById(R.id.item_recycler_restaurant_hours_textview);
            distance = itemView.findViewById(R.id.item_recycler_restaurant_distance_textview);
            //workmatesCount = itemView.findViewById(R.id.item_recycler_restaurant_workmates_textview);
            workmatesIcon = itemView.findViewById(R.id.item_recycler_restaurant_workmates_imageview);
            //stars = itemView.findViewById(R.id.item_recycler_restaurant_stars_imageview);
            picture = itemView.findViewById(R.id.item_recycler_restaurant_picture_imageview);
        }
    }
}
