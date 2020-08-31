package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.cliquet.gautier.go4lunch.Controllers.Activities.MainActivity;
import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Controllers.RestaurantClickCallback;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.Sort;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment implements RestaurantClickCallback {

    private RecyclerView recyclerView;
    private List<Restaurant> mRestaurantList = new ArrayList<>();

    private SearchView searchView = null;

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

        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("I'm Hungry!");

        Gson gson = new Gson();

        if (getArguments() != null) {
            if(getArguments().getString("restaurant_list") != null) {
                String gsonRestaurantList = getArguments().getString("restaurant_list");
                mRestaurantList = gson.fromJson(gsonRestaurantList, new TypeToken<List<Restaurant>>() {}.getType());
                Sort sort = new Sort();

                mRestaurantList = sort.sortList(mRestaurantList, getArguments().getInt("sort_type"));
                recyclerView = view.findViewById(R.id.activity_restaurants_list_recycler);
                setRecyclerView();
            }
        }

        return view;
    }

    private void setRecyclerView() {
        ListRecyclerAdapter adapter = new ListRecyclerAdapter(this.getContext(),  mRestaurantList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.toolbar_menu_search_item);
        SearchManager searchManger = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);

        if(item != null) {
            searchView = (SearchView) item.getActionView();
        }
        if(searchView != null) {
            assert searchManger != null;
            searchView.setSearchableInfo(searchManger.getSearchableInfo(getActivity().getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClicked(int position, Restaurant restaurant) {
        Intent restaurantDetailsActivityIntent = new Intent(getContext(), RestaurantDetails.class);
        restaurantDetailsActivityIntent.putExtra("restaurant", restaurant);
        startActivity(restaurantDetailsActivityIntent);
    }
}
