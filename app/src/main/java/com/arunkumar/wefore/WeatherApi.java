package com.arunkumar.wefore;

import com.arunkumar.wefore.model.Weather;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("forecast.json?key=d158568a87e84a72849171052192006&days=5")
    Single<Weather> getWeatherForecast(@Query(value = "q", encoded = true) String latlong);
}
