package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import androidx.annotation.NonNull;

import com.cliquet.gautier.go4lunch.BuildConfig;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GoogleMapsLocationService {

    String apiKey = BuildConfig.GOOGLE_MAPS_API_KEY;

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl httpUrl = original.url();

                    HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("key", apiKey).build();

                    Request.Builder requestBuilder = original.newBuilder().url(newHttpUrl);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            })
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbySearchPojo> getNearbySearch(@QueryMap Map<String, String> map);

    @GET("maps/api/place/textsearch/json")
    Call<NearbySearchPojo> getFromText(@QueryMap Map<String, String> map);

    @GET("maps/api/place/details/json")
    //Call<DetailsPojo> getDetails(@QueryMap Map<String, String> map);
    Call<DetailsPojo> getDetails(@Query("placeid") String placeId);
}
