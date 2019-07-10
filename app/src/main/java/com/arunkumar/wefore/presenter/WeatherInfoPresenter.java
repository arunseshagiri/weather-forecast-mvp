package com.arunkumar.wefore.presenter;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.arunkumar.wefore.R;
import com.arunkumar.wefore.WeatherApiService;
import com.arunkumar.wefore.view.WeatherInfoView;

import java.lang.ref.WeakReference;

import static com.arunkumar.wefore.utils.Observers.withSingleLogger;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class WeatherInfoPresenter implements LocationListener {

    private WeakReference<WeatherInfoView> view;
    private String baseUrl;
    private LocationManager locationManager;

    public void onCreate(String baseUrl, WeatherInfoView view) {
        this.view = new WeakReference<>(view);
        this.baseUrl = baseUrl;
    }

    public void onStart() {
        if (view != null && view.get() != null) {
            view.get().showProgress();
        }
        view.get().checkPermission();
    }

    private void fetchWeather(String baseUrl, Location location) {
        WeatherApiService
                .getWeatherForecast(baseUrl, location)
                .observeOn(mainThread())
                .doOnSuccess(weather -> view.get().updateCurrentTemperature(weather.getCurrent().temp()))
                .doOnSuccess(weather -> view.get().updateCurrentLocation(weather.getLocation().getName()))
                .subscribeOn(io())
                .map(weather -> weather.getForecast().forecastDay())
                .observeOn(mainThread())
                .doOnSuccess(__ -> view.get().hideProgress())
                .doOnSuccess(__ -> view.get().hideError())
                .doOnSuccess(forecastDaysList -> view.get().updateWeatherList(forecastDaysList))
                .doOnError(__ -> view.get().showError())
                .subscribe(withSingleLogger());
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdate(boolean hasPermission) {
        if (!hasPermission) {
            return;
        }
        locationManager = view.get().getLocationManager();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showInfoDialog();
        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, true);
            locationManager
                    .requestLocationUpdates(provider, 0, 0, this);
        }
    }

    private void showInfoDialog() {
        view.get().showDialog(
                R.string.title,
                R.string.message,
                (dialogInterface, i) -> view.get().launchSettings(),
                (dialogInterface, i) -> view.get().showError()
        );
    }

    @Override
    public void onLocationChanged(Location location) {
        fetchWeather(baseUrl, location);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
