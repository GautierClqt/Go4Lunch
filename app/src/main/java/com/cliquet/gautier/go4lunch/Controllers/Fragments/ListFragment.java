package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Controllers.Callback;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements Callback {

    private RecyclerView recyclerView;

    private List<Restaurant> mRestaurantList = new ArrayList<>();
    //private List<NearbySearchPojo.NearbySearchResults> mNearbySearchResults = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, viewGroup, false);

        Gson gson = new Gson();

        if (getArguments() != null) {
            String gsonRestaurantList = getArguments().getString("restaurant_list");
            mRestaurantList = gson.fromJson(gsonRestaurantList, new TypeToken<List<Restaurant>>(){}.getType());

            //A EFFACER!!!
//            if(mRestaurantList != null){
//                mNearbySearchResults = mRestaurantList.getNearbySearchResults();
//            }
        }

        recyclerView = view.findViewById(R.id.activity_restaurants_list_recycler);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(),  mRestaurantList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClicked(int position, Restaurant restaurant) {
        Gson gson = new Gson();

        Intent restaurantDetailsActivityIntent = new Intent(getContext(), RestaurantDetails.class);
        restaurantDetailsActivityIntent.putExtra("restaurant", restaurant);

        startActivity(restaurantDetailsActivityIntent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
