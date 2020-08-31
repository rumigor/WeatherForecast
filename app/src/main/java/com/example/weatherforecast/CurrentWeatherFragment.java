package com.example.weatherforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class CurrentWeatherFragment extends Fragment {

    private static final String WEATHER = "Weather_Parameters";
    TextView cityName;
    TextView temperature;
    ImageView weatherCondition;


    private Weather weather;

    public static CurrentWeatherFragment create(Weather weather) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER, weather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = (Weather) getArguments().getSerializable(WEATHER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityName = view.findViewById(R.id.cityName2);
        temperature = view.findViewById(R.id.currentTemp2);
        weatherCondition = view.findViewById(R.id.imageView2);
        if (weather != null) {
            cityName.setText(weather.getCityName());
            temperature.setText(weather.getTemperature());
            setPic(weather.getwCond());
        }
        if (savedInstanceState != null){
            weather = (Weather) savedInstanceState.getSerializable(WEATHER);
            cityName.setText(weather.getCityName());
            temperature.setText(weather.getTemperature());
            setPic(weather.getwCond());
        }
    }

    private void setPic(int wCond) {
        switch (wCond){
            case 1:
                weatherCondition.setImageResource(R.drawable.sun2);
                break;
            case 2:
                weatherCondition.setImageResource(R.drawable.party_cloudy);
                break;
            case 3:
                weatherCondition.setImageResource(R.drawable.cloudy);
                break;
            case 4:
                weatherCondition.setImageResource(R.drawable.rainy);
                break;
            case 5:
                weatherCondition.setImageResource(R.drawable.thunderstorm);
                break;
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int wCond = picEquals(weatherCondition);
        weather = new Weather(cityName.getText().toString(), temperature.getText().toString(), wCond);
        outState.putSerializable(WEATHER, weather);
    }

    private int picEquals(ImageView mainImage) {
        if (mainImage.getDrawable().getConstantState() == getActivity()
                .getResources().getDrawable(R.drawable.sun2)
                .getConstantState()) {
            return 1;
        } else if (mainImage.getDrawable().getConstantState() == getActivity()
                .getResources().getDrawable(R.drawable.party_cloudy)
                .getConstantState()) {
            return 2;
        } else if ((mainImage.getDrawable().getConstantState() == getActivity()
                .getResources().getDrawable(R.drawable.cloudy)
                .getConstantState())) {
            return 3;
        } else if (mainImage.getDrawable().getConstantState() == getActivity()
                .getResources().getDrawable(R.drawable.rainy)
                .getConstantState()) {
            return 4;
        } else if (mainImage.getDrawable().getConstantState() == getActivity()
                .getResources().getDrawable(R.drawable.thunderstorm)
                .getConstantState()) {
            return 5;
        } else return 0;
    }
}