package com.example.weatherforecast;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;


public class ForecastFragment extends Fragment {

    private static final String CITY_NAME = "CityName";

    private String cityName;
    private TextView day;
    private  TextView temperature;
    private ImageView weatherCondition;

    final WeatherAdapter weatherAdapter = new WeatherAdapter();


    private final Forecast[] spbDays = {new Forecast("Monday", "+18°C", 2), new Forecast("Tuesday", "+19°C", 1),
            new Forecast("Wednesday", "+16°", 4), new Forecast("Thursday", "+19°C", 1), new Forecast("Friday", "+18°C", 2), new Forecast("Saturday", "+14°C", 4),
            new Forecast("Sunday", "+17°C", 3)};


    private final Forecast[] vlnDays = {new Forecast("Monday", "+20°C", 1), new Forecast("Tuesday", "+22°C", 5),
            new Forecast("Wednesday", "+16°", 4), new Forecast("Thursday", "+21°C", 2), new Forecast("Friday", "+18°C", 4), new Forecast("Saturday", "+17°C", 3),
            new Forecast("Sunday", "+17°C", 3)};

    private final Forecast[] bcnDays = {new Forecast("Monday", "+28°C", 2), new Forecast("Tuesday", "+31°C", 1),
            new Forecast("Wednesday", "+26°", 3), new Forecast("Thursday", "+27°C", 2), new Forecast("Friday", "+28°C", 1), new Forecast("Saturday", "+23°C", 4),
            new Forecast("Sunday", "+22°C", 3)};




    public ForecastFragment() {
        // Required empty public constructor
    }


    public static ForecastFragment create(String cityName) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString(CITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.weatherRecyclerView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        recyclerView.setAdapter(weatherAdapter);
        if (savedInstanceState != null){
            cityName = savedInstanceState.getString(CITY_NAME);
        }
        if (cityName == null){
            cityName = getString(R.string.spb);
        }
        if (cityName.equals(getString(R.string.spb))){
            weatherAdapter.setDays(Arrays.asList(spbDays));
        }
        else if (cityName.equals(getString(R.string.vln))){
            weatherAdapter.setDays(Arrays.asList(vlnDays));
        }
        else if (cityName.equals(getString(R.string.bcn))){
            weatherAdapter.setDays(Arrays.asList(bcnDays));
        }
    }


}