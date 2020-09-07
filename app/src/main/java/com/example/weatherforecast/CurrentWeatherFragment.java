package com.example.weatherforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


public class CurrentWeatherFragment extends Fragment {

    private static final String CITY_NAME ="CityName";
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    private String cityName;


    private EditText city;
    private EditText temperature;
    private EditText pressure;
    private EditText humidity;
    private EditText windSpeed;




    public static CurrentWeatherFragment create(String cityName) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
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
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    private void init(View view){
        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemprature);
        pressure = view.findViewById(R.id.textPressure);
        humidity =view.findViewById(R.id.textHumidity);
        windSpeed = view.findViewById(R.id.textWindspeed);
        if (cityName == null){
            cityName = "Saint Petersburg,RU";
        }
        final Handler handler = new Handler();
        final Data data = new Data(cityName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WeatherRequest weatherRequest = data.getData();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayWeather(weatherRequest);
                    }
                    private void displayWeather(WeatherRequest weatherRequest){
                        city.setText(weatherRequest.getName());
                        temperature.setText(String.format("%f2", weatherRequest.getMain().getTemp()-273));
                        pressure.setText(String.format("%d", weatherRequest.getMain().getPressure()));
                        humidity.setText(String.format("%d", weatherRequest.getMain().getHumidity()));
                        windSpeed.setText(String.format("%f2", weatherRequest.getWind().getSpeed()));
                    }
                });
            }
        }).start();

        Button refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (cityName == null){
                    cityName = "Saint Petersburg,RU";
                }
                final String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", cityName, BuildConfig.WEATHER_API_KEY);
                final URL uri = new URL(url);
                final Handler handler = new Handler(); // Запоминаем основной поток
                final Data data = new Data(cityName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final WeatherRequest weatherRequest = data.getData();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest);
                            }
                            private void displayWeather(WeatherRequest weatherRequest){
                                city.setText(weatherRequest.getName());
                                temperature.setText(String.format("%f2", weatherRequest.getMain().getTemp()-273));
                                pressure.setText(String.format("%d", weatherRequest.getMain().getPressure()));
                                humidity.setText(String.format("%d", weatherRequest.getMain().getHumidity()));
                                windSpeed.setText(String.format("%f2", weatherRequest.getWind().getSpeed()));
                            }
                        });
                    }
                }).start();
            } catch (MalformedURLException e) {
                Log.e(TAG, "Fail URI", e);
                e.printStackTrace();
            }
        }

        private String getLines(BufferedReader in) {
            return in.lines().collect(Collectors.joining("\n"));
        }
    };

}