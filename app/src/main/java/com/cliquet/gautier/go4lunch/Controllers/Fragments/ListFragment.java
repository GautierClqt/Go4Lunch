package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Controllers.Callback;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements Callback {

    private RecyclerView recyclerView;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public ListFragment() {
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
        }

        recyclerView = view.findViewById(R.id.activity_restaurants_list_recycler);
        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        RestaurantsRecyclerAdapter adapter = new RestaurantsRecyclerAdapter(this.getContext(),  mRestaurantList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.toolbar_menu_search_item);
        SearchManager searchManger = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if(item != null) {
            searchView = (SearchView) item.getActionView();
        }
        if(searchView != null) {
            searchView.setSearchableInfo(searchManger.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    mRestaurantList.clear();
                    setRecyclerView();
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
        Intent restaurantDetailsActivityIntent = new Intent(getContext(), RestaurantDetails.class);
        restaurantDetailsActivityIntent.putExtra("restaurant", restaurant);
        startActivity(restaurantDetailsActivityIntent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
