package com.arunkumar.wefore.model;

import com.arunkumar.wefore.json_adapters.DateToWeekDayElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class ForecastDay {

    @SerializedName("date")
    @JsonAdapter(DateToWeekDayElement.class)
    private String dayOfWeek;

    @SerializedName("day")
    private Day day;

    public ForecastDay dayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public ForecastDay day(Day day) {
        this.day = day;
        return this;
    }

    public String dayOfWeek() {
        return dayOfWeek;
    }

    public Day day() {
        return day;
    }
}
