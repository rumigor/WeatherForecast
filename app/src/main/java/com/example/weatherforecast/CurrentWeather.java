package com.example.weatherforecast;

import android.content.SharedPreferences;
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

import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;
import com.example.weatherforecast.thermometerView.Thermometer;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class CurrentWeather extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String WEATHER = "WEATHER";

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
    private static final String CITY_NAME ="CityName";
    private WeatherRequest weatherRequest;

    public CurrentWeather() {
        // Required empty public constructor
    }

    public static CurrentWeather create(WeatherRequest weatherRequest) {
        CurrentWeather fragment = new CurrentWeather();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER, weatherRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           weatherRequest  = (WeatherRequest)getArguments().getSerializable(WEATHER);
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
        init(view);
        displayCurrentWeather(weatherRequest);
    }

    private void init(View view){
        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemprature);
        pressure = view.findViewById(R.id.textPressure);
        humidity =view.findViewById(R.id.textHumidity);
        windSpeed = view.findViewById(R.id.textWindspeed);
        currentWeather = view.findViewById(R.id.weatherIco);
        weatherCondition = view.findViewById(R.id.textWeatherCondition);
        thermometer = view.findViewById(R.id.thermometer);
        dateTime = view.findViewById(R.id.dateTime);
        metrics = Metrics.getInstance();
    }

    private void displayCurrentWeather(WeatherRequest weatherRequest){
        if (!metrics.isFahrenheit()) {
            thermometer.changeUnit(true);
            thermometer.setCurrentTemp(weatherRequest.getMain().getTemp() - 273.15f);
            temperature.setText(String.format("%+.1f%s", weatherRequest.getMain().getTemp() - 273.15f, "°C"));
        }
        else {
            thermometer.changeUnit(false);
            float temp = (weatherRequest.getMain().getTemp()-273.15f)*1.8f+32;
            thermometer.setCurrentTemp(temp);
            temperature.setText(String.format("%+.1f%s", temp, "°F"));

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM \nHH:mm", Locale.getDefault());
        Date date = new Date(weatherRequest.getDt()*1000);
        dateTime.setText(sdf.format(date));
        city.setText(weatherRequest.getName());
        SharedPreferences sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CITY_NAME, weatherRequest.getName()).commit();
        pressure.setText(String.format("%.1f %s", weatherRequest.getMain().getPressure() / 1.33f, getString(R.string.pressureValue)));
        humidity.setText(String.format("%d%s", weatherRequest.getMain().getHumidity(), "%"));
        windSpeed.setText(String.format("%.1f %s %s", weatherRequest.getWind().getSpeed(), getString(R.string.windSpeedValue), getWindDirection(weatherRequest.getWind().getDeg())));
        weatherCondition.setText(weatherRequest.getWeather()[0].getDescription());
        String imageURL = String.format("https://openweathermap.org/img/wn/%s@4x.png", weatherRequest.getWeather()[0].getIcon());
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