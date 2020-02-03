package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class PlacesApiCalls {

    public interface Callback {
        void onResponse(NearbySearchPojo pojoMain);
        void onFailure();
    }

    public static void fetchLocations(Callback callback, HashMap<String, String> mapQueries) {

        final WeakReference<Callback> callbacksWeakReference = new WeakReference<>(callback);
        GoogleMapsLocationService googleMapLocationService = GoogleMapsLocationService.retrofit.create(GoogleMapsLocationService.class);

        Call<NearbySearchPojo> call;
        call = googleMapLocationService.getGoogleMapDatas(mapQueries);

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
}
