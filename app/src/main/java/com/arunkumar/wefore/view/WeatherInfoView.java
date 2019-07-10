package com.arunkumar.wefore.view;

import android.content.DialogInterface;
import android.location.LocationManager;

import com.arunkumar.wefore.model.ForecastDay;

import java.util.List;

public interface WeatherInfoView {

    void showProgress();

    void hideProgress();

    void updateWeatherList(List<ForecastDay> forecastDays);

    void showError();

    void hideError();

    void updateCurrentTemperature(String temp);

    void updateCurrentLocation(String location);

    void showDialog(
            int title,
            int message,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener);

    void checkPermission();

    void launchSettings();

    LocationManager getLocationManager();

}
