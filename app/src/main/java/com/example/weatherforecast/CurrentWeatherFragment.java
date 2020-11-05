package com.example.weatherforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
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
    private ImageView currentWeather;




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
        currentWeather = view.findViewById(R.id.currentWeather);
        dataLoading(cityName);
        Button refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           dataLoading(cityName);
        }

        private String getLines(BufferedReader in) {
            return in.lines().collect(Collectors.joining("\n"));
        }
    };

    private void dataLoading(String cityName){
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
                        try {
                            displayWeather(weatherRequest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    private void displayWeather(WeatherRequest weatherRequest) throws IOException {
                        city.setText(weatherRequest.getName());
                        temperature.setText(String.format("%d%s", (int) weatherRequest.getMain().getTemp()-273, "°C"));
                        pressure.setText(String.format("%d %s", (int) (weatherRequest.getMain().getPressure()/1.33), getString(R.string.pressureValue)));
                        humidity.setText(String.format("%d%s", weatherRequest.getMain().getHumidity(), "%"));
                        windSpeed.setText(String.format("%d %s", (int) weatherRequest.getWind().getSpeed(), getString(R.string.windSpeedValue)));
                        Picasso.with(getContext()).load(String.format("http://openweathermap.org/img/wn/%s@4x.png", weatherRequest.getWeather()[0].getIcon())).into(currentWeather);
                    }
                });
            }
        }).start();
    }

}