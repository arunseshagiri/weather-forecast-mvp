package com.arunkumar.wefore;

import android.content.Context;
import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class WeatherApiServiceTest {
    private static final String MOCK_RESPONSE = "{\n" +
            "    \"location\": {\n" +
            "        \"name\": \"Bangalore\",\n" +
            "        \"region\": \"Karnataka\",\n" +
            "        \"country\": \"India\",\n" +
            "        \"lat\": 12.98,\n" +
            "        \"lon\": 77.58,\n" +
            "        \"tz_id\": \"Asia/Kolkata\",\n" +
            "        \"localtime_epoch\": 1561207513,\n" +
            "        \"localtime\": \"2019-06-22 18:15\"\n" +
            "    },\n" +
            "    \"current\": {\n" +
            "        \"last_updated_epoch\": 1561206638,\n" +
            "        \"last_updated\": \"2019-06-22 18:00\",\n" +
            "        \"temp_c\": 28.0,\n" +
            "        \"temp_f\": 82.4,\n" +
            "        \"is_day\": 1,\n" +
            "        \"condition\": {\n" +
            "            \"text\": \"Partly cloudy\",\n" +
            "            \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\",\n" +
            "            \"code\": 1003\n" +
            "        },\n" +
            "        \"wind_mph\": 9.4,\n" +
            "        \"wind_kph\": 15.1,\n" +
            "        \"wind_degree\": 220,\n" +
            "        \"wind_dir\": \"SW\",\n" +
            "        \"pressure_mb\": 1008.0,\n" +
            "        \"pressure_in\": 30.2,\n" +
            "        \"precip_mm\": 0.4,\n" +
            "        \"precip_in\": 0.02,\n" +
            "        \"humidity\": 62,\n" +
            "        \"cloud\": 50,\n" +
            "        \"feelslike_c\": 29.9,\n" +
            "        \"feelslike_f\": 85.9,\n" +
            "        \"vis_km\": 6.0,\n" +
            "        \"vis_miles\": 3.0,\n" +
            "        \"uv\": 7.0,\n" +
            "        \"gust_mph\": 20.6,\n" +
            "        \"gust_kph\": 33.1\n" +
            "    },\n" +
            "    \"forecast\": {\n" +
            "        \"forecastday\": [\n" +
            "            {\n" +
            "                \"date\": \"2019-06-22\",\n" +
            "                \"date_epoch\": 1561161600,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 25.9,\n" +
            "                    \"maxtemp_f\": 78.6,\n" +
            "                    \"mintemp_c\": 22.2,\n" +
            "                    \"mintemp_f\": 72.0,\n" +
            "                    \"avgtemp_c\": 23.4,\n" +
            "                    \"avgtemp_f\": 74.1,\n" +
            "                    \"maxwind_mph\": 17.4,\n" +
            "                    \"maxwind_kph\": 28.1,\n" +
            "                    \"totalprecip_mm\": 1.4,\n" +
            "                    \"totalprecip_in\": 0.06,\n" +
            "                    \"avgvis_km\": 10.0,\n" +
            "                    \"avgvis_miles\": 6.0,\n" +
            "                    \"avghumidity\": 76.0,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Light rain shower\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/353.png\",\n" +
            "                        \"code\": 1240\n" +
            "                    },\n" +
            "                    \"uv\": 6.3\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"05:55 AM\",\n" +
            "                    \"sunset\": \"06:48 PM\",\n" +
            "                    \"moonrise\": \"10:51 PM\",\n" +
            "                    \"moonset\": \"09:58 AM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2019-06-23\",\n" +
            "                \"date_epoch\": 1561248000,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 25.9,\n" +
            "                    \"maxtemp_f\": 78.6,\n" +
            "                    \"mintemp_c\": 20.9,\n" +
            "                    \"mintemp_f\": 69.6,\n" +
            "                    \"avgtemp_c\": 23.3,\n" +
            "                    \"avgtemp_f\": 73.9,\n" +
            "                    \"maxwind_mph\": 16.1,\n" +
            "                    \"maxwind_kph\": 25.9,\n" +
            "                    \"totalprecip_mm\": 4.0,\n" +
            "                    \"totalprecip_in\": 0.16,\n" +
            "                    \"avgvis_km\": 9.8,\n" +
            "                    \"avgvis_miles\": 6.0,\n" +
            "                    \"avghumidity\": 74.0,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Moderate or heavy rain shower\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/356.png\",\n" +
            "                        \"code\": 1243\n" +
            "                    },\n" +
            "                    \"uv\": 6.6\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"05:55 AM\",\n" +
            "                    \"sunset\": \"06:48 PM\",\n" +
            "                    \"moonrise\": \"11:31 PM\",\n" +
            "                    \"moonset\": \"10:46 AM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2019-06-24\",\n" +
            "                \"date_epoch\": 1561334400,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 28.3,\n" +
            "                    \"maxtemp_f\": 82.9,\n" +
            "                    \"mintemp_c\": 20.2,\n" +
            "                    \"mintemp_f\": 68.4,\n" +
            "                    \"avgtemp_c\": 23.8,\n" +
            "                    \"avgtemp_f\": 74.8,\n" +
            "                    \"maxwind_mph\": 15.2,\n" +
            "                    \"maxwind_kph\": 24.5,\n" +
            "                    \"totalprecip_mm\": 18.1,\n" +
            "                    \"totalprecip_in\": 0.71,\n" +
            "                    \"avgvis_km\": 8.4,\n" +
            "                    \"avgvis_miles\": 5.0,\n" +
            "                    \"avghumidity\": 75.0,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Moderate or heavy rain shower\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/356.png\",\n" +
            "                        \"code\": 1243\n" +
            "                    },\n" +
            "                    \"uv\": 11.5\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"05:55 AM\",\n" +
            "                    \"sunset\": \"06:49 PM\",\n" +
            "                    \"moonrise\": \"No moonrise\",\n" +
            "                    \"moonset\": \"11:33 AM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2019-06-25\",\n" +
            "                \"date_epoch\": 1561420800,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 27.6,\n" +
            "                    \"maxtemp_f\": 81.7,\n" +
            "                    \"mintemp_c\": 21.2,\n" +
            "                    \"mintemp_f\": 70.2,\n" +
            "                    \"avgtemp_c\": 23.8,\n" +
            "                    \"avgtemp_f\": 74.9,\n" +
            "                    \"maxwind_mph\": 15.7,\n" +
            "                    \"maxwind_kph\": 25.2,\n" +
            "                    \"totalprecip_mm\": 20.3,\n" +
            "                    \"totalprecip_in\": 0.8,\n" +
            "                    \"avgvis_km\": 8.6,\n" +
            "                    \"avgvis_miles\": 5.0,\n" +
            "                    \"avghumidity\": 74.0,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Moderate or heavy rain shower\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/356.png\",\n" +
            "                        \"code\": 1243\n" +
            "                    },\n" +
            "                    \"uv\": 11.4\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"05:56 AM\",\n" +
            "                    \"sunset\": \"06:49 PM\",\n" +
            "                    \"moonrise\": \"12:08 AM\",\n" +
            "                    \"moonset\": \"12:19 PM\"\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    private MockWebServer mServer;
    private CountDownLatch mLatch;
    private Context context;
    private Location location;

    @Before
    public void setup() {
        setupRxThreadsForTest();
        context = mock(Context.class);
        location = mock(Location.class);
        mServer = new MockWebServer();
        doReturn(String.valueOf(mServer.url("/"))).when(context).getString(R.string.base_url);
        mLatch = new CountDownLatch(1);
    }

    @Test
    public void getWeatherInfo() throws InterruptedException {
        mServer.enqueue(new MockResponse().setBody(MOCK_RESPONSE));


        WeatherApiService
                .getWeatherForecast(context, location)
                .subscribe(
                        weather -> {
                            assertEquals(weather.getLocation().getName(), "Bangalore");
                            assertEquals(weather.getCurrent().temp(), "28");
                            assertEquals(weather.getForecast().forecastDay().size(), 3);
                            assertEquals(weather.getForecast().forecastDay().get(0).dayOfWeek(), "Sunday");
                            assertEquals(weather.getForecast().forecastDay().get(0).day().avgTemp(), "23");
                            mLatch.countDown();
                        },
                        error -> {
                            fail(error.getMessage());
                            mLatch.countDown();
                        }
                );
        mServer.takeRequest();
    }

    @Test
    public void failureToGetWeatherInfoForWrongKey() throws InterruptedException {
        mServer.enqueue(new MockResponse().setBody("").setResponseCode(401));
        WeatherApiService
                .getWeatherForecast(context, location)
                .subscribe(
                        weather -> {
                            assertNull(weather);
                            mLatch.countDown();
                        },
                        error -> {
                            assertEquals(error.getMessage(), "HTTP 401 Client Error");
                            mLatch.countDown();
                        }
                );
        mServer.takeRequest();
    }

    @Test
    public void failureToGetWeatherInfoWithInvalidQuery() throws InterruptedException {
        mServer.enqueue(new MockResponse().setBody("").setResponseCode(400));
        WeatherApiService
                .getWeatherForecast(context, location)
                .subscribe(
                        weather -> {
                            assertNull(weather);
                            mLatch.countDown();
                        },
                        error -> {
                            assertEquals(error.getMessage(), "HTTP 400 Client Error");
                            mLatch.countDown();
                        }
                );
        mServer.takeRequest();
    }

    private void setupRxThreadsForTest() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(handler -> Schedulers.trampoline());
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(handler -> Schedulers.trampoline());
    }

    @After
    public void tearDown() throws Exception {
        mServer.takeRequest(200, TimeUnit.MILLISECONDS);
        mServer.shutdown();
    }


}
