package com.example.weatherforecast;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.forecast.ForecastData;
import com.example.weatherforecast.forecast.ForecastRequest;
import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;


public class CurrentWeatherFragment extends Fragment {

    private static final String CITY_NAME ="CityName";
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    private String cityName;
    final WeatherAdapter weatherAdapter = new WeatherAdapter();


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
        final RecyclerView recyclerView = view.findViewById(R.id.weatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),  LinearLayoutManager.HORIZONTAL);
        itemDecoration.setDrawable(requireActivity().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weatherAdapter);
    }

    private void init(View view){
        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemprature);
        pressure = view.findViewById(R.id.textPressure);
        humidity =view.findViewById(R.id.textHumidity);
        windSpeed = view.findViewById(R.id.textWindspeed);
        currentWeather = view.findViewById(R.id.currentWeather);
        dataLoading(cityName);
    }


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
                if (weatherRequest != null) {
                    final ForecastData forecastData = new ForecastData(weatherRequest.getCoord().getLat(), weatherRequest.getCoord().getLon());
                    final ForecastRequest forecastRequest = forecastData.getData();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                displayWeather(weatherRequest);
                                displayForecast(forecastRequest);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        private void displayWeather(WeatherRequest weatherRequest) throws IOException {
                            city.setText(weatherRequest.getName());
                            temperature.setText(String.format("%d%s", (int) weatherRequest.getMain().getTemp() - 273, "°C"));
                            pressure.setText(String.format("%d %s", (int) (weatherRequest.getMain().getPressure() / 1.33), getString(R.string.pressureValue)));
                            humidity.setText(String.format("%d%s", weatherRequest.getMain().getHumidity(), "%"));
                            windSpeed.setText(String.format("%d %s", (int) weatherRequest.getWind().getSpeed(), getString(R.string.windSpeedValue)));
                            Picasso.with(getContext()).load(String.format("http://openweathermap.org/img/wn/%s@4x.png", weatherRequest.getWeather()[0].getIcon())).into(currentWeather);
                        }

                        private void displayForecast(ForecastRequest forecastRequest1) {
                            ArrayList<Forecast> forecasts = new ArrayList<>();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
                            for (int i = 0; i < forecastRequest1.getDaily().length; i++) {
                                Date date = new Date(forecastRequest1.getDaily()[i].getDt() * 1000);
                                String dText = sdf.format(date);
                                String dayTemp = String.format("%d%s", (int) (forecastRequest1.getDaily()[i].getTemp().getDay() - 273), "°C");
                                String nightTemp = String.format("%d%s", (int) (forecastRequest1.getDaily()[i].getTemp().getNight() - 273), "°C");
                                String weatherIco = forecastRequest1.getDaily()[i].getWeather()[0].getIcon();
                                Forecast forecast = new Forecast(dText, dayTemp, nightTemp, weatherIco);
                                forecasts.add(forecast);
                            }
                            weatherAdapter.setDays(forecasts);
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         Toast.makeText(getContext(), getString(R.string.cityNotFound), Toast.LENGTH_LONG).show();
                                     }
                                 });

                }

            }
        }).start();
    }

}