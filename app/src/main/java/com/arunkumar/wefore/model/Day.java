package com.arunkumar.wefore.model;

import com.arunkumar.wefore.json_adapters.DoubleToIntegerStringElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class Day {
    @SerializedName("avgtemp_c")
    @JsonAdapter(DoubleToIntegerStringElement.class)
    private String avgTemp;

    public Day avgTemp(String avgTemp) {
        this.avgTemp = avgTemp;
        return this;
    }

    public String avgTemp() {
        return avgTemp;
    }
}
