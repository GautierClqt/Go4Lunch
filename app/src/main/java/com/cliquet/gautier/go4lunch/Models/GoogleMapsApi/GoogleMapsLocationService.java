package com.cliquet.gautier.go4lunch.Models.GoogleMapsApi;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import com.cliquet.gautier.go4lunch.BuildConfig;
import com.cliquet.gautier.go4lunch.Models.Pojo.GoogleMapsPojo;
import com.cliquet.gautier.go4lunch.R;

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
import retrofit2.http.QueryMap;

public interface GoogleMapsLocationService {

    //String apiKey = Resources.getSystem().getString(R.string.google_maps_key);
//    String apiKey =

    //add api-key to every url
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(new Interceptor() {
//                @NonNull
//                @Override
//                public Response intercept(@NonNull Chain chain) throws IOException {
//                    Request original = chain.request();
//                    HttpUrl httpUrl = original.url();
//
//                    HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("key", apiKey).build();
//
//                    Request.Builder requestBuilder = original.newBuilder().url(newHttpUrl);
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            })
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("maps/api/place/nearbysearch/json")
    Call<GoogleMapsPojo> getGoogleMapDatas(@QueryMap Map<String, String> map);
}
