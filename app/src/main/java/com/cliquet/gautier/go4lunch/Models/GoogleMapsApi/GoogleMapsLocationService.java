package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.util.Map;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GoogleMapsLocationService {

    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbySearchPojo> getGoogleMapDatas(@QueryMap Map<String, String> map);

    @GET("maps/api/place/details/json")
    Call<DetailsPojo> getDetails(@QueryMap Map<String, String> map);
}
