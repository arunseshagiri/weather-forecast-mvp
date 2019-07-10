package com.arunkumar.wefore.adapter;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherRecyclerViewAdapterModule {

    @Provides
    public WeatherRecyclerViewAdapter provideForecastDays() {
        return new WeatherRecyclerViewAdapter();
    }
}
