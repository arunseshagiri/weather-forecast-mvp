package com.arunkumar.wefore.json_adapters;

import com.arunkumar.wefore.model.ForecastDay;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RemoveCurrentDayTempElement implements JsonDeserializer<List<ForecastDay>> {

    @Override
    public List<ForecastDay> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Type collectionType = new TypeToken<List<ForecastDay>>() {
        }.getType();
        Gson gson = new Gson();
        List<ForecastDay> forecastDays = gson.fromJson(json, collectionType);
        if (forecastDays.size() > 0) {
            forecastDays.remove(0);
        }
        return forecastDays;
    }
}
