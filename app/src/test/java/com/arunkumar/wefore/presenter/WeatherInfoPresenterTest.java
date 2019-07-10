package com.arunkumar.wefore.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.arunkumar.wefore.R;
import com.arunkumar.wefore.WeatherApiService;
import com.arunkumar.wefore.model.Current;
import com.arunkumar.wefore.model.CurrentLocation;
import com.arunkumar.wefore.model.Day;
import com.arunkumar.wefore.model.Forecast;
import com.arunkumar.wefore.model.ForecastDay;
import com.arunkumar.wefore.model.Weather;
import com.arunkumar.wefore.view.WeatherInfoView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({WeatherApiService.class, ActivityCompat.class})
public class WeatherInfoPresenterTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private Context context;
    private Activity activity;
    private WeatherInfoView view;
    private LocationManager locationManager;
    private Location location;

    private WeatherInfoPresenter presenter;

    @Before
    public void setup() {
        context = mock(Context.class);
        activity = mock(Activity.class);
        view = mock(WeatherInfoView.class);
        locationManager = mock(LocationManager.class);
        location = mock(Location.class);

        when(context.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager);

        presenter = spy(new WeatherInfoPresenter());
        presenter.onCreate(context, activity, view);
    }

    @Test
    public void testOnStart() {
        presenter.onStart();
        verify(view, times(1)).showProgress();
        verify(presenter, times(1)).checkPermissionAndFetchWeatherInfo();
    }

    @Test
    public void testFetchWeatherSuccessCase() {
        setupRxThreadsForTest();
        List<ForecastDay> forecastDays = new ArrayList<>();
        forecastDays.add(new ForecastDay().dayOfWeek("Monday").day(new Day().avgTemp("20")));
        Current current = new Current().temp("10");
        Forecast forecast = new Forecast().forecastDay(forecastDays);
        CurrentLocation currentLocation = new CurrentLocation().name("Bangalore");
        Weather weather = new Weather().current(current).forecast(forecast).location(currentLocation);

        Single<Weather> weatherSingle = Single.just(weather);
        mockStatic(WeatherApiService.class);
        when(WeatherApiService.getWeatherForecast(context, location)).thenReturn(weatherSingle);
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        presenter.onStart();
        presenter.onLocationChanged(location);

        verify(view, times(1)).updateCurrentTemperature("10");
        verify(view, times(1)).updateCurrentLocation("Bangalore");
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).hideError();
        verify(view, times(1)).updateWeatherList(forecastDays);
        verify(view, never()).showError();
    }

    @Test
    public void testFetchWeatherFailureCase() {
        setupRxThreadsForTest();
        Single<Weather> weatherSingleError = Single.error(new Throwable());
        mockStatic(WeatherApiService.class);
        when(WeatherApiService.getWeatherForecast(context, location)).thenReturn(weatherSingleError);
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        presenter.onStart();
        presenter.onLocationChanged(location);

        verify(view, never()).updateCurrentTemperature(anyString());
        verify(view, never()).updateCurrentLocation(anyString());
        verify(view, never()).hideProgress();
        verify(view, never()).hideError();
        verify(view, never()).updateWeatherList(any(List.class));
        verify(view, times(1)).showError();
    }

    @Test
    public void testShowInfoDialogToTurnOnLocation() {
        when(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        presenter.onStart();
        ArgumentCaptor<DialogInterface.OnClickListener> positiveArgCaptor
                = ArgumentCaptor.forClass(DialogInterface.OnClickListener.class);
        ArgumentCaptor<DialogInterface.OnClickListener> negativeArgCaptor
                = ArgumentCaptor.forClass(DialogInterface.OnClickListener.class);
        verify(view, times(1))
                .showDialog(
                        eq(context),
                        eq(R.string.title),
                        eq(R.string.message),
                        positiveArgCaptor.capture(),
                        negativeArgCaptor.capture()
                );

        positiveArgCaptor.getValue().onClick(null, 0);
        verify(context, times(1)).startActivity(any(Intent.class));

        negativeArgCaptor.getValue().onClick(null, 0);
        verify(view, times(1)).showError();
    }

    @Test
    public void testPermissionAlreadyGranted() {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", 25);
        when(context.checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);
        when(context.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);
        presenter.checkPermissionAndFetchWeatherInfo();

        verify(presenter, times(1)).requestLocationUpdate(true);
    }

    private void setupRxThreadsForTest() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(handler -> Schedulers.trampoline());
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(handler -> Schedulers.trampoline());
    }

}
