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
import com.cliquet.gautier.go4lunch.Controllers.Callback;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;

import java.util.List;

public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Workmates> mWorkmatesList;

    public WorkmatesRecyclerAdapter(Context context, List<Workmates> workmatesList) {
        this.mContext = context;
        this.mWorkmatesList = workmatesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_workmates, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text.setText(mWorkmatesList.get(i).getFirstName() + " is connected");
        Glide.with(viewHolder.picture).load(R.drawable.restaurant_phone).into(viewHolder.picture);

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
}
