package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            currentWeatherFragment = (CurrentWeatherFragment)
                    getSupportFragmentManager().findFragmentById(R.id.currentWeather);
            forecastFragment = (ForecastFragment)
                    getSupportFragmentManager().findFragmentById(R.id.container);

        } else if (currentWeatherFragment == null && forecastFragment == null) {
            currentWeatherFragment = new CurrentWeatherFragment();
            forecastFragment = new ForecastFragment();
        }
        if (!currentWeatherFragment.isInLayout()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.currentWeather, currentWeatherFragment)
                    .commit();
        }
        if (!forecastFragment.isInLayout()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, forecastFragment)
                    .commit();
        }
    }
}