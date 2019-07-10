package com.arunkumar.wefore.presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherInfoPresenterModule {
    @Provides
    public WeatherInfoPresenter presenter() {
        return new WeatherInfoPresenter();
    }
}
