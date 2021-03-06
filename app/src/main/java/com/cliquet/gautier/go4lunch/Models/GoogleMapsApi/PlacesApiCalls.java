package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class PlacesApiCalls {

    public interface GoogleMapsCallback {
        void onResponse(NearbySearchPojo nearbySearchPojo);
        void onResponse(DetailsPojo detailsPojo, int index);
        void onFailure();
    }

    public static void fetchNearbySearch(GoogleMapsCallback googleMapsCallback, HashMap<String, String> mapQueries) {

        final WeakReference<GoogleMapsCallback> callbacksWeakReference = new WeakReference<>(googleMapsCallback);
        GoogleMapsLocationService googleMapLocationService = GoogleMapsLocationService.retrofit.create(GoogleMapsLocationService.class);

        Call<NearbySearchPojo> call;
        call = googleMapLocationService.getNearbySearch(mapQueries);

        call.enqueue(new retrofit2.Callback<NearbySearchPojo>() {
            @Override
            public void onResponse(Call<NearbySearchPojo> call, Response<NearbySearchPojo> response) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<NearbySearchPojo> call, Throwable t) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    public static void fetchFromText(GoogleMapsCallback googleMapsCallback, HashMap<String, String> mapQueries) {
        final WeakReference<GoogleMapsCallback> callbacksWeakReference = new WeakReference<>(googleMapsCallback);
        GoogleMapsLocationService googleMapLocationService = GoogleMapsLocationService.retrofit.create(GoogleMapsLocationService.class);

        Call<NearbySearchPojo> call;
        call = googleMapLocationService.getFromText(mapQueries);

        call.enqueue(new retrofit2.Callback<NearbySearchPojo>() {
            @Override
            public void onResponse(Call<NearbySearchPojo> call, Response<NearbySearchPojo> response) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<NearbySearchPojo> call, Throwable t) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    public static void fetchDetails(GoogleMapsCallback detailsCallback, String placeId, final int index) {
        final WeakReference<GoogleMapsCallback> callbackskWeakReference = new WeakReference<>(detailsCallback);
        GoogleMapsLocationService googleMapsLocationService = GoogleMapsLocationService.retrofit.create(GoogleMapsLocationService.class);

        Call<DetailsPojo> call;
        call = googleMapsLocationService.getDetails(placeId);

        call.enqueue(new retrofit2.Callback<DetailsPojo>() {
            @Override
            public void onResponse(Call<DetailsPojo> call, Response<DetailsPojo> response) {
                if(callbackskWeakReference.get() != null) callbackskWeakReference.get().onResponse(response.body(), index);
            }

            @Override
            public void onFailure(Call<DetailsPojo> call, Throwable t) {
                if(callbackskWeakReference.get() != null) callbackskWeakReference.get().onFailure();
            }
        });


    }
}
