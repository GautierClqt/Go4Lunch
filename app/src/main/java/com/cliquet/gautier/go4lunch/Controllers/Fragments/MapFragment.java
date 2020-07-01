package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Controllers.Activities.RestaurantDetails;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
    private List<MarkerOptions> mMarkersList = new ArrayList<>();

    private double mUserLat;
    private double mUserLng;

    private String clickOnMarkerTitle = "";

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
    }

    // TODO: Rename and change types and number of parameters
    private static MapFragment newInstance() {
        return new MapFragment();
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

        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        return view;
    }

    private void getMarkersOnList(List<DocumentSnapshot> selectedRestaurantlist) {
        mMarkersList.clear();

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_red_24dp);
        for(int i = 0; i < mRestaurantList.size(); i++) {
            for (int j = 0; j < selectedRestaurantlist.size(); j++) {
                if (selectedRestaurantlist.get(j).getId().equals(mRestaurantList.get(i).getId())) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_green_24dp);
                    break;
                } else {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_place_red_24dp);
                }
            }

            double placeLat = mRestaurantList.get(i).getLatitude();
            double placeLong = mRestaurantList.get(i).getLongitude();
            String placeName = mRestaurantList.get(i).getName();
            LatLng marker = new LatLng(placeLat, placeLong);
            mMarkersList.add(new MarkerOptions().position(marker)
                    .title(placeName).icon(icon));
        }

        if(mRestaurantList.size() != 0) {
            addMarkersOnMap();
        }
    }

    private void clickOnAMarker(Marker marker) {
        if(marker.getTitle().equals(clickOnMarkerTitle)) {
            for (int i = 0; i < mRestaurantList.size(); i++) {
                if (marker.getTitle().equals(mRestaurantList.get(i).getName())) {
                    Log.d("marker", mRestaurantList.get(i).getId());
                    clickOnMarkerTitle = "";
                    Intent restaurantDetailsActivityIntent = new Intent(getContext(), RestaurantDetails.class);
                    restaurantDetailsActivityIntent.putExtra("restaurant", mRestaurantList.get(i));
                    startActivity(restaurantDetailsActivityIntent);
                    break;
                }
            }
        } else {
            marker.showInfoWindow();
            clickOnMarkerTitle = marker.getTitle();
        }
    }

    private void getUserLocation() {
        FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getActivity()));
        location.getLastLocation().addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mUserLat = location.getLatitude();
                mUserLng = location.getLongitude();
                LatLng latlng = new LatLng(mUserLat, mUserLng);
                setCameraOnMap(mGoogleMaps, latlng);
            }
        });
    }

    private void getRestaurantList() {
        Gson gson = new Gson();

        if (getArguments() != null) {
            if(getArguments().getString("restaurant_list") != null) {
                String gsonRestaurantList = getArguments().getString("restaurant_list");
                mRestaurantList = gson.fromJson(gsonRestaurantList, new TypeToken<List<Restaurant>>(){}.getType());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference restaurantRef = db.collection("restaurants");

                restaurantRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> selectedRestaurantlist = Objects.requireNonNull(task.getResult()).getDocuments();

                        getMarkersOnList(selectedRestaurantlist);
                    }
                });
            }
        }

    }

    private void setCameraOnMap(GoogleMap googleMap, LatLng latLng) {
        LatLngBounds.Builder bld = new LatLngBounds.Builder();

        for(int i = 0; i < mRestaurantList.size(); i++) {
            LatLng ll = new LatLng(mRestaurantList.get(i).getLatitude(), mRestaurantList.get(i).getLongitude());
            bld.include(ll);
        }

        bld.include(latLng);
        LatLngBounds bound = bld.build();
        mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 100));
    }

    private void addMarkersOnMap() {
        mGoogleMaps.clear();
        for(int i = 0; i < mMarkersList.size(); i++) {
            mGoogleMaps.addMarker(mMarkersList.get(i));
        }

        mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clickOnAMarker(marker);
                return true;
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
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

        getRestaurantList();
        getUserLocation();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
