package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Api.RestaurantHelper;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Workmates> mWorkmatesList;

    public WorkmatesRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mWorkmatesList = new ArrayList<>();
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
        Glide.with(viewHolder.picture).load(mWorkmatesList.get(i).getUrlPicture()).into(viewHolder.picture);
    }


    @Override
    public int getItemCount() {
        return mWorkmatesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.item_recycler_workmates_picture_imageview);
            text = itemView.findViewById(R.id.item_recycler_workmates_textview_textview);
        }
    }

    public void setWorkmatesList(List<Workmates> workmates) {
        this.mWorkmatesList = workmates;
        notifyDataSetChanged();
    }

    private void sortWorkmatesList() {
        int listSize = mWorkmatesList.size();
        for(int i = 0; i < listSize; i++) {
            if(mWorkmatesList.get(i).getSelectedRestaurant() == null) {
                for(int j = i; j < listSize; j++) {
                    if(mWorkmatesList.get(j).getSelectedRestaurant() != null) {
                        Workmates tempsWorkmates = mWorkmatesList.get(i);
                        mWorkmatesList.set(i, mWorkmatesList.get(j));
                        mWorkmatesList.set(j, tempsWorkmates);
                    }
                }
            }
        }
    }

    public void setViewholderText(ViewHolder viewHolder, String restaurantName, int i) {
        String text = mContext.getResources().getString(R.string.workmates_has_chosen, mWorkmatesList.get(i).getFirstName(), restaurantName);
        viewHolder.text.setText(text);
    }

}
