package com.arunkumar.wefore;

import android.Manifest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class WeatherInfoActivityTest {

    @Rule
    public ActivityTestRule<WeatherInfoActivity> activityTestRule
            = new ActivityTestRule<>(WeatherInfoActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void setup() {
        activityTestRule.launchActivity(null);
    }

    @After
    public void finish() {
    }

    @Test
    public void checkViews() {
        onView(withId(R.id.current_temperature)).check(matches(isDisplayed()));
        onView(withId(R.id.current_temperature_degree)).check(matches(isDisplayed()));
        onView(withId(R.id.current_location)).check(matches(isDisplayed()));

        onView(withId(R.id.weather_recycler_view))
                .check(matches(hasDescendant(withId(R.id.day_of_the_week))));
        onView(withId(R.id.weather_recycler_view))
                .check(matches(hasDescendant(withId(R.id.temperature))));
    }
}
