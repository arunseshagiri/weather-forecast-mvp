package com.arunkumar.wefore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arunkumar.wefore.R;
import com.arunkumar.wefore.model.ForecastDay;

import java.util.List;

import static android.view.View.GONE;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ForecastDay> forecastDays;

    public WeatherRecyclerViewAdapter() {
    }

    public void forecastDays(List<ForecastDay> forecastDays) {
        this.forecastDays = forecastDays;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_forecast_list_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final WeatherViewHolder weatherViewHolder = (WeatherViewHolder) holder;
        weatherViewHolder.dayOfTheWeek.setText(forecastDays.get(position).dayOfWeek());
        weatherViewHolder.temperature.setText(forecastDays.get(position).day().avgTemp() + " C");
        if (position == getItemCount() - 1) {
            weatherViewHolder.seperator.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return forecastDays.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        final View view;
        final TextView dayOfTheWeek;
        final TextView temperature;
        final View seperator;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            dayOfTheWeek = view.findViewById(R.id.day_of_the_week);
            temperature = view.findViewById(R.id.temperature);
            seperator = view.findViewById(R.id.separator);
        }
    }
}
