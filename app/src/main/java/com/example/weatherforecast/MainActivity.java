package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String WEATHER = "Weather_Parameters";
    private final static int REQUEST_CODE = 0x1FAB;
    private final static int REQUEST_CODE_SET = 0x2FAB;
    private static final String NIGHT_THEME = "darkTheme";
    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;
    Weather weather;
    Button chgCity;
    Button settings;
    private boolean nightTheme;



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
            if (!forecastFragment.isInLayout()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, forecastFragment)
                        .commit();
            }
        chgCity = findViewById(R.id.cityChanger);
        chgCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityChangerActivity.class);
                intent.putExtra(NIGHT_THEME, nightTheme);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        final String city;
        if (weather == null){
            city = getString(R.string.spb);
        }
        else city = weather.getCityName();
        Button goToWeb = findViewById(R.id.openInternet);
        goToWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                if (city.equals(getString(R.string.spb))){
                    url = "https://www.gismeteo.ru/weather-sankt-peterburg-4079/";
                }
                else if (city.equals(getString(R.string.vln))){
                    url = "https://www.gismeteo.ru/weather-vilnius-4230/";
                }
                else if (city.equals(getString(R.string.bcn))){
                    url = "https://www.gismeteo.ru/weather-barcelona-1948/";
                }
                else url = "https://www.gismeteo.ru/";
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);

            }
        });
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsIntent.putExtra(NIGHT_THEME, nightTheme);
                startActivityForResult(settingsIntent, REQUEST_CODE_SET);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            weather = (Weather) data.getExtras().getSerializable(WEATHER);
            currentWeatherFragment = (CurrentWeatherFragment)getSupportFragmentManager().findFragmentById(R.id.currentWeather);
            currentWeatherFragment = CurrentWeatherFragment.create(weather);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.currentWeather, currentWeatherFragment)
                    .commit();
            forecastFragment = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.container);
            forecastFragment = ForecastFragment.create(weather.getCityName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, forecastFragment)
                    .commit();
        }
        if (resultCode == 100){
            nightTheme = data.getExtras().getBoolean(NIGHT_THEME);
            ConstraintLayout layout = findViewById(R.id.mainLayout);
            if (nightTheme){
                layout.setBackgroundColor(Color.rgb(0, 85, 124));
            }
            else {
                layout.setBackgroundColor(Color.rgb(0, 188, 212));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NIGHT_THEME, nightTheme);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nightTheme = savedInstanceState.getBoolean(NIGHT_THEME);
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        if (nightTheme){
            layout.setBackgroundColor(Color.rgb(0, 85, 124));
        }
        else {
            layout.setBackgroundColor(Color.rgb(0, 188, 212));
        }
    }
}