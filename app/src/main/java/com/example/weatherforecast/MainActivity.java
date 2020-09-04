package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private static final String WEATHER = "Weather_Parameters";
    private final static int REQUEST_CODE = 0x1FAB;
    private final static int REQUEST_CODE_SET = 0x2FAB;
    private static final String NIGHT_THEME = "darkTheme";
    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;
    Weather weather;
    Button chgCity;
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


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_dashboard:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(settingsIntent, REQUEST_CODE_SET);
                        return true;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        return true;
                }
                return false;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        return super.onOptionsItemSelected(item);
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
            recreate();
//            nightTheme = data.getExtras().getBoolean(NIGHT_THEME);
//            ConstraintLayout layout = findViewById(R.id.mainLayout);
//            if (nightTheme){
//                setTheme(R.style.AppDarkTheme);;
//            }
//            else {
//                setTheme(R.style.AppTheme);;
//            }
        }
    }

}