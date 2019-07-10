package com.arunkumar.wefore.model;

import com.arunkumar.wefore.json_adapters.DoubleToIntegerStringElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("temp_c")
    @JsonAdapter(DoubleToIntegerStringElement.class)
    private String temp;

    public Current temp(String temp) {
        this.temp = temp;
        return this;
    }

    public String temp() {
        return temp;
    }
}
