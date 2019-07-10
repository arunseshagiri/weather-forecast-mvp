package com.arunkumar.wefore;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arunkumar.wefore.adapter.WeatherRecyclerViewAdapter;
import com.arunkumar.wefore.model.ForecastDay;
import com.arunkumar.wefore.presenter.WeatherInfoPresenter;
import com.arunkumar.wefore.utils.DialogUtility;
import com.arunkumar.wefore.utils.GenericUtils;
import com.arunkumar.wefore.view.WeatherInfoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WeatherInfoActivity extends AppCompatActivity implements WeatherInfoView {

    @Inject
    WeatherInfoPresenter presenter;
    @Inject
    WeatherRecyclerViewAdapter adapter;

    private View weatherInfoLayout;
    private View errorLayout;
    private RecyclerView weatherRecyclerView;
    private TextView currentTemperatureTv;
    private TextView currentLocationTv;
    private ImageView loadingIv;

    private static final int PERMISSIONS_REQ_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherInfoLayout = findViewById(R.id.weather_info_layout);
        errorLayout = findViewById(R.id.error_layout);
        weatherRecyclerView = findViewById(R.id.weather_recycler_view);
        currentTemperatureTv = findViewById(R.id.current_temperature);
        currentLocationTv = findViewById(R.id.current_location);
        loadingIv = findViewById(R.id.ic_loading);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DaggerWeatherInfoComponent.builder().build().inject(this);

        adapter.forecastDays(new ArrayList<>());
        weatherRecyclerView.setAdapter(adapter);
        presenter.onCreate(getString(R.string.base_url), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void showProgress() {
        loadingIv.setVisibility(VISIBLE);
        weatherInfoLayout.setVisibility(GONE);
        errorLayout.setVisibility(GONE);
        Animation animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        loadingIv.startAnimation(animRotate);
    }

    @Override
    public void hideProgress() {
        loadingIv.clearAnimation();
        loadingIv.setVisibility(GONE);
        weatherInfoLayout.setVisibility(VISIBLE);
    }

    @Override
    public void updateWeatherList(List<ForecastDay> forecastDays) {
        adapter.forecastDays(forecastDays);
        weatherRecyclerView.setAdapter(adapter);
        weatherRecyclerView.getAdapter().notifyDataSetChanged();
        weatherRecyclerView.setTranslationY(GenericUtils.getScreenHeight(this));
        weatherRecyclerView.animate().translationY(0).setDuration(1500).start();
    }

    public void retry(View view) {
        showProgress();
        checkPermission();
    }

    @Override
    public void showError() {
        errorLayout.setVisibility(VISIBLE);
        weatherInfoLayout.setVisibility(GONE);

    }

    @Override
    public void hideError() {
        errorLayout.setVisibility(GONE);
        weatherInfoLayout.setVisibility(VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateCurrentTemperature(String temp) {
        currentTemperatureTv.setText(temp);
    }

    @Override
    public void updateCurrentLocation(String location) {
        currentLocationTv.setText(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQ_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        presenter.requestLocationUpdate(true);
                    } else {
                        showError();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions != null && !permissions.isEmpty()) {
                ActivityCompat.requestPermissions(
                        this,
                        permissions.toArray(new String[permissions.size()]),
                        PERMISSIONS_REQ_CODE
                );
            } else {
                presenter.requestLocationUpdate(true);
            }
        } else {
            presenter.requestLocationUpdate(true);
        }
    }

    @Override
    public void launchSettings() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public LocationManager getLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void showDialog(
            int title,
            int message,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {
        DialogUtility.showDialog(this, title, message, positiveListener, negativeListener);
    }
}
