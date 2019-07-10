package com.arunkumar.wefore.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("location")
    private CurrentLocation location;
    @SerializedName("current")
    private Current current;
    @SerializedName("forecast")
    private Forecast forecast;

    public Weather location(CurrentLocation location) {
        this.location = location;
        return this;
    }

    public Weather current(Current current) {
        this.current = current;
        return this;
    }

    public Weather forecast(Forecast forcast) {
        this.forecast = forcast;
        return this;
    }

    public CurrentLocation getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public Forecast getForecast() {
        return forecast;
    }
}
