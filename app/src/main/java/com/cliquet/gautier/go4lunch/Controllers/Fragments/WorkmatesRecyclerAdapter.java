package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cliquet.gautier.go4lunch.Api.RestaurantHelper;
import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Workmates> mWorkmatesList;
    private List<Restaurant> mRestaurantsList;

    public WorkmatesRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mWorkmatesList = new ArrayList<>();
        this.mRestaurantsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_workmates, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int index = i;

        sortWorkmatesList();

        if(mWorkmatesList.get(i).getSelectedRestaurant() == null) {
            mWorkmatesList.get(i).getFirstName();
            String text = mContext.getResources().getString(R.string.workmates_hasnt_chose, mWorkmatesList.get(i).getFirstName());
            viewHolder.text.setText(text);
            viewHolder.text.setTextColor(Color.parseColor("#999999"));
            viewHolder.text.setTypeface(viewHolder.text.getTypeface(), Typeface.ITALIC);
        }
        else {
            RestaurantHelper.getRestaurant(mWorkmatesList.get(i).getSelectedRestaurant()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    final String restaurantName = task.getResult().get("name").toString();
                    setViewholderText(viewHolder, restaurantName, index);
                }
            });

        }
        Glide.with(viewHolder.picture).load(mWorkmatesList.get(i).getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(viewHolder.picture);

        viewHolder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantHelper.getRestaurant(mWorkmatesList.get(viewHolder.getAdapterPosition()).getSelectedRestaurant()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final String restaurantId = task.getResult().getId();
                        for(int i = 0; i < mRestaurantsList.size(); i++) {
                            if(mRestaurantsList.get(i).getId().equals(restaurantId)) {
                                Restaurant restaurant = mRestaurantsList.get(i);
                                startRestaurantDetailsActivity(restaurant);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkmatesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mainlayout;
        ImageView picture;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainlayout = itemView.findViewById(R.id.item_recycler_workmates_mainlayout);
            picture = itemView.findViewById(R.id.item_recycler_workmates_picture_imageview);
            text = itemView.findViewById(R.id.item_recycler_workmates_textview_textview);
        }
    }

    public void setWorkmatesList(List<Workmates> workmates) {
        this.mWorkmatesList = workmates;
        notifyDataSetChanged();
    }

    public void setRestaurantsList(List<Restaurant> restaurants) {
        this.mRestaurantsList = restaurants;
    }

    private void sortWorkmatesList() {

        ArrayList<Workmates> choseList = new ArrayList<>();
        ArrayList<Workmates> notChoseList = new ArrayList<>();

        for(int i = 0; i < mWorkmatesList.size(); i++) {
            if(mWorkmatesList.get(i).getSelectedRestaurant() != null) {
                choseList.add(mWorkmatesList.get(i));
            }
            else {
                notChoseList.add(mWorkmatesList.get(i));
            }
        }
        mWorkmatesList.clear();
        mWorkmatesList.addAll(choseList);
        mWorkmatesList.addAll(notChoseList);
    }

    private void startRestaurantDetailsActivity(Restaurant restaurant) {
        if(restaurant != null) {
            Intent restaurantDetailsActivityIntent = new Intent(mContext, RestaurantDetails.class);
            restaurantDetailsActivityIntent.putExtra("restaurant", restaurant);
            mContext.startActivity(restaurantDetailsActivityIntent);
        }
    }

    public void setViewholderText(ViewHolder viewHolder, String restaurantName, int i) {
        String text = mContext.getResources().getString(R.string.workmates_has_chosen, mWorkmatesList.get(i).getFirstName(), restaurantName);
        viewHolder.text.setText(text);
    }

}
