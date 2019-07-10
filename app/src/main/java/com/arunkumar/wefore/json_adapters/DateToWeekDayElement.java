package com.arunkumar.wefore.json_adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateToWeekDayElement implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = df.parse(json.getAsString());
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return simpleDateformat.format(date);
        } catch (final java.text.ParseException e) {
            return "";
        }
    }
}
