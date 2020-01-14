package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import com.cliquet.gautier.go4lunch.Models.Pojo.GoogleMapsPojo;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class GoogleMapCalls {

    public interface Callback {
        void onResponse(GoogleMapsPojo pojoMain);
        void onFailure();
    }

    public static void fetchLocations(Callback callback, HashMap<String, String> mapQueries) {

        final WeakReference<Callback> callbacksWeakReference = new WeakReference<>(callback);
        GoogleMapsLocationService googleMapLocationService = GoogleMapsLocationService.retrofit.create(GoogleMapsLocationService.class);

        Call<GoogleMapsPojo> call;
        call = googleMapLocationService.getGoogleMapDatas(mapQueries);

        call.enqueue(new retrofit2.Callback<GoogleMapsPojo>() {
            @Override
            public void onResponse(Call<GoogleMapsPojo> call, Response<GoogleMapsPojo> response) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
                int test = 1;
            }

            @Override
            public void onFailure(Call<GoogleMapsPojo> call, Throwable t) {
                if(callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });

    }
}
