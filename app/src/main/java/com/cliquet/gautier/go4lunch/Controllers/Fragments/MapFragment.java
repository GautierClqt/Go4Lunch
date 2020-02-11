package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private GoogleMap mGoogleMaps;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    //private NearbySearchPojo mNearbySearchPojo = new NearbySearchPojo();
    private List<NearbySearchPojo.NearbySearchResults> mResults = new ArrayList<>();
    private String nextPageToken;
    private int i;
    private double mUserLat;
    private double mUserLng;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Gson gson = new Gson();

        String gsonRestaurantList = getArguments().getString("restaurant_list");
        mRestaurantList = gson.fromJson(gsonRestaurantList, new TypeToken<List<Restaurant>>(){}.getType());

        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    private void getUserLocation() {
        FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getActivity()));
        location.getLastLocation().addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mUserLat = location.getLatitude();
                mUserLng = location.getLongitude();
                LatLng latlng = new LatLng(mUserLat, mUserLng);
                setCameraOnUser(mGoogleMaps, latlng);

                if(mRestaurantList.size() != 0) {
                    putPinsOnPlaces(mRestaurantList);
                }
            }
        });
    }

    private void setCameraOnUser(GoogleMap googleMap, LatLng latLng) {
        float zoomLevel = 15.5f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    private void putPinsOnPlaces(List<Restaurant> mRestaurantList) {

        for(int i = 0; i < mRestaurantList.size(); i++) {
            double placeLat = mRestaurantList.get(i).getLatitude();
            double placeLong = mRestaurantList.get(i).getLongitude();
            String placeName = mRestaurantList.get(i).getName();
            LatLng marker = new LatLng(placeLat, placeLong);
            mGoogleMaps.addMarker(new MarkerOptions().position(marker).title(placeName));
        }
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
    public void onPoiClick(PointOfInterest pointOfInterest) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMaps = googleMap;
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.setOnPoiClickListener(this);

        getUserLocation();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
