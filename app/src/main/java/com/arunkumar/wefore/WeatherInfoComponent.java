package com.arunkumar.wefore;

import com.arunkumar.wefore.adapter.WeatherRecyclerViewAdapterModule;
import com.arunkumar.wefore.presenter.WeatherInfoPresenterModule;

import dagger.Component;

@Component(modules = {WeatherInfoPresenterModule.class, WeatherRecyclerViewAdapterModule.class})
public interface WeatherInfoComponent {
    void inject(WeatherInfoActivity weatherInfoActivity);
}
