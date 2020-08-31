package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityChangerActivity extends AppCompatActivity {
    private final String WEATHER = "Weather_Parameters";
    Weather weather;
    Forecast forecast;
    final CityAdapter cityAdapter = new CityAdapter();
    EditText search;
    final ArrayList<String> cities = new ArrayList<String>();
    final ArrayList<String> citiesNew = new ArrayList<String>();
    private static final String NIGHT_THEME = "darkTheme";
    private boolean nightMode;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_changer);
        layout = findViewById(R.id.cityLayout);
        if (getIntent().getExtras() != null){
            nightMode = getIntent().getExtras().getBoolean(NIGHT_THEME);
        }
        if (nightMode){
            layout.setBackgroundColor(Color.rgb(0, 85, 124));
        }
        else {
            layout.setBackgroundColor(Color.rgb(0, 188, 212));
        }
        cities.addAll((Arrays.asList(getString(R.string.spb), getString(R.string.vln), getString(R.string.bcn), getString(R.string.msc), getString(R.string.bru))));
        citiesNew.addAll(cities);
        final RecyclerView recyclerView = findViewById(R.id.cityRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(cityAdapter);
        updateCities(cities);
        cityAdapter.setOnCityClickListener(new CityAdapter.OnCityClickListener() {
            @Override
            public void onClicked(String city) {
                Intent intent = new Intent(CityChangerActivity.this, MainActivity.class);
                if (city.equals(cities.get(0))) {
                    weather = new Weather(city, "+15°C", 4);
                }
                else if (city.equals(cities.get(1))){
                    weather = new Weather(city, "+18°C", 3);
                }
                else if (city.equals(cities.get(2))){
                    weather = new Weather(city, "+29°C", 1);
                }
                else if (city.equals(cities.get(3))){
                    weather = new Weather(city, "+22°C", 2);
                }
                else if (city.equals(cities.get(4))){
                    weather = new Weather(city, "+16°C", 3);
                }
                else {
                    weather = new Weather(city, "no data", 1);
                }
                intent.putExtra(WEATHER, weather);
                setResult(RESULT_OK, intent);
                finish();
            }

        });
        search = findViewById(R.id.cityType);
        final Button addCity = findViewById(R.id.searchButton);
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search.getText() != null) {
                    citiesNew.add(search.getText().toString());
                }
                cityAdapter.setCities(citiesNew);
                search.setText("");
            }
        });
        final Button remove = findViewById(R.id.resetButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCities(cities);
            }
        });
    }

    private void updateCities(List<String> cities) {
        cityAdapter.setCities(cities);
    }

    private void removeCities(List<String> removeCities){
        cityAdapter.setCities(removeCities);
        citiesNew.clear();
        citiesNew.addAll(cities);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("CITIES_LIST", cities);
        outState.putBoolean(NIGHT_THEME, nightMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cities.clear();
        cities.addAll(savedInstanceState.getStringArrayList("CITIES_LIST"));
        nightMode = savedInstanceState.getBoolean(NIGHT_THEME);
        if (nightMode){
            layout.setBackgroundColor(Color.rgb(0, 85, 124));
        }
        else {
            layout.setBackgroundColor(Color.rgb(0, 188, 212));
        }
    }
}