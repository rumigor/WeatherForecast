package com.example.weatherforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherforecast.forecastModel.ForecastRequest;
import com.example.weatherforecast.thermometerView.Thermometer;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class ForecastFragment extends Fragment {


    private static final String FORECAST = "FORECAST";
    private static final String INDEX = "INDEX";
    private static final String CITY_NAME = "CITY_NAME";

    private ForecastRequest forecastRequest;
    private int i;

    private EditText city;
    private EditText temperature;
    private EditText pressure;
    private EditText humidity;
    private EditText windSpeed;
    private ImageView currentWeather;
    private Thermometer thermometer;
    private EditText weatherCondition;
    private TextView dateTime;
    private Metrics metrics;
    private String cityName;

    public ForecastFragment() {
        // Required empty public constructor
    }


    public static ForecastFragment create(ForecastRequest forecastRequest, int i, String cityName) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORECAST, forecastRequest);
        args.putInt(INDEX, i);
        args.putString(CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forecastRequest = (ForecastRequest)getArguments().getSerializable(FORECAST);
            i = getArguments().getInt(INDEX);
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
        init(view);
        displayWeather(forecastRequest);
    }

    private void init(View view){
        city = view.findViewById(R.id.textCityF);
        temperature = view.findViewById(R.id.textTempratureF);
        pressure = view.findViewById(R.id.textPressureF);
        humidity =view.findViewById(R.id.textHumidityF);
        windSpeed = view.findViewById(R.id.textWindspeedF);
        currentWeather = view.findViewById(R.id.wCondForecast);
        weatherCondition = view.findViewById(R.id.textWeatherConditionF);
        thermometer = view.findViewById(R.id.thermometerForecast);
        dateTime = view.findViewById(R.id.dayForecast);
        metrics = Metrics.getInstance();
    }

    private void displayWeather(ForecastRequest forecastRequest){
        if (!metrics.isFahrenheit()) {
            thermometer.changeUnit(true);
            thermometer.setCurrentTemp(forecastRequest.getDaily()[i].getTemp().getDay() - 273.15f);
            temperature.setText(String.format("%s %+.1f%s / %s %+.1f%s", getString(R.string.day), forecastRequest.getDaily()[i].getTemp().getDay() - 273.15f, "°C", getString(R.string.night), forecastRequest.getDaily()[i].getTemp().getNight() - 273.15f, "°C"));
        }
        else {
            thermometer.changeUnit(false);
            float temp = (forecastRequest.getDaily()[i].getTemp().getDay()-273.15f)*1.8f+32;
            float tempN = (forecastRequest.getDaily()[i].getTemp().getNight()-273.15f)*1.8f+32;
            thermometer.setCurrentTemp(temp);
            temperature.setText(String.format("%s %+.1f%s / %s %+.1f%s", getString(R.string.day), temp, "°F", getString(R.string.night), tempN, "°F"));

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
        Date date = new Date(forecastRequest.getDaily()[i].getDt()*1000);
        dateTime.setText(sdf.format(date));
        city.setText(cityName);
        pressure.setText(String.format("%.1f %s", forecastRequest.getDaily()[i].getPressure() / 1.33f, getString(R.string.pressureValue)));
        humidity.setText(String.format("%d%s", forecastRequest.getDaily()[i].getHumidity(), "%"));
        windSpeed.setText(String.format("%.1f %s %s", forecastRequest.getDaily()[i].getWind_speed(), getString(R.string.windSpeedValue), getWindDirection(weatherRequest.getWind().getDeg())));
        weatherCondition.setText(forecastRequest.getDaily()[i].getWeather()[0].getDescription());
        String imageURL = String.format("https://openweathermap.org/img/wn/%s@4x.png", forecastRequest.getDaily()[i].getWeather()[0].getIcon());
        Picasso.get().load(imageURL)
                .error(R.drawable.cloudy)
                .into(currentWeather);
    }
    private String getWindDirection(int degree){
        if (degree < 23 || degree >= 338) return getString(R.string.North);
        else if (degree < 68) return getString(R.string.NorthEast);
        else if (degree < 113) return getString(R.string.East);
        else if (degree < 158) return getString(R.string.SouthEast);
        else if (degree < 203) return getString(R.string.South);
        else if (degree < 248) return getString(R.string.SouthWest);
        else if (degree < 293) return getString(R.string.West);
        else return getString(R.string.NorthWest);
    }
}