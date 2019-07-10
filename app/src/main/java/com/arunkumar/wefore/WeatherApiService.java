package com.arunkumar.wefore;

import android.location.Location;

import com.arunkumar.wefore.model.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherApiService {

    private String baseUrl;

    private WeatherApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private Retrofit getClient() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient())
                .build();
    }

    private OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    private static Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    private WeatherApi getApi() {
        return getClient().create(WeatherApi.class);
    }

    public static Single<Weather> getWeatherForecast(String baseUrl, Location location) {
        return new WeatherApiService(baseUrl).getApi()
                .getWeatherForecast(location.getLatitude() + "," + location.getLongitude())
                .subscribeOn(Schedulers.io());
    }

}
