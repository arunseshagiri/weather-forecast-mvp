package com.arunkumar.wefore.model;

import com.arunkumar.wefore.json_adapters.RemoveCurrentDayTempElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    @JsonAdapter(RemoveCurrentDayTempElement.class)
    private List<ForecastDay> forecastDay;

    public Forecast forecastDay(List<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
        return this;
    }

    public List<ForecastDay> forecastDay() {
        return forecastDay;
    }
}
