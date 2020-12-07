package com.example.weatherforecast;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.forecastModel.ForecastRequest;
import com.example.weatherforecast.forecastRecycleView.WeatherAdapter;
import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;
import com.example.weatherforecast.roomDataBase.App;
import com.example.weatherforecast.roomDataBase.GetStoryData;
import com.example.weatherforecast.roomDataBase.Story;
import com.example.weatherforecast.roomDataBase.StoryDao;
import com.example.weatherforecast.roomDataBase.StorySource;
import com.example.weatherforecast.thermometerView.Thermometer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class WeatherFragment extends Fragment {

    private static final String CITY_NAME ="CityName";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private SharedPreferences sharedPref;
    private String cityName;
    final WeatherAdapter weatherAdapter = new WeatherAdapter();
    static final String BROADCAST_GET_DATA = "GET_DATA";
    private float lat;
    private float lon;


    private Metrics metrics;
    private StorySource storySource;
    private WeatherRequest weatherRequest;
    private ForecastRequest forecastRequest;





    public static WeatherFragment create(String cityName, float lat, float lon) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        args.putFloat(LATITUDE, lat);
        args.putFloat(LONGITUDE, lon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().registerReceiver(getDataReceiver, new IntentFilter(BROADCAST_GET_DATA));
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().unregisterReceiver(getDataReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString(CITY_NAME);
            lat = getArguments().getFloat(LATITUDE);
            lon = getArguments().getFloat(LONGITUDE);
        }
        else {
            SharedPreferences sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
            cityName = sharedPref.getString(CITY_NAME, cityName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initNotificationChannel();
        final RecyclerView recyclerView = view.findViewById(R.id.weatherRecyclerView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));}
        else {recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));}
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),  LinearLayoutManager.HORIZONTAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.separator)));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weatherAdapter);
        if (savedInstanceState != null){
            metrics = Metrics.getInstance();
            weatherRequest = (WeatherRequest) savedInstanceState.getSerializable("CURRENT_WEATHER");
            forecastRequest = (ForecastRequest) savedInstanceState.getSerializable("FORECAST");
            if (weatherRequest != null) {loadCurrentWeather();}
            if (forecastRequest != null) {displayForecast(forecastRequest);}
        } else dataLoading(cityName, lat, lon);

    }



    //Загружаем информацию
    private void dataLoading(String cityName, float lat, float lon){
        GetDataService.startGetDataService(getContext(), cityName, lat, lon);
    }
    private BroadcastReceiver getDataReceiver = new BroadcastReceiver() { //получаем данные от сервиса
        @Override
        public void onReceive(Context context, Intent intent) {
            weatherRequest = (WeatherRequest) intent.getSerializableExtra(GetDataService.CURRENT_WEATHER);
            forecastRequest = (ForecastRequest) intent.getSerializableExtra(GetDataService.FORECAST_DATA);
            final Handler handler = new Handler();
            if (weatherRequest == null || forecastRequest == null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.exclamation)
                                .setMessage(R.string.cityNotFound)
                                .setIcon(R.drawable.title_small)
                                .setCancelable(false)
                                .setPositiveButton(R.string.button,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
            else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        metrics = Metrics.getInstance();
                        try {
                            displayWeather(weatherRequest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        displayForecast(forecastRequest);
                    }
                });

            }
        }
    };
    private void displayWeather(WeatherRequest weatherRequest) throws IOException { //отображаем погоду, в зависимости от настроек (°С или °F)
        loadCurrentWeather();
        new Thread(() -> {
            StoryDao storyDao = App
                    .getInstance()
                    .getStoryDao();
            storySource = new StorySource(storyDao);
            List<Story> cities = storySource.getStoryList();
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).city.equals(weatherRequest.getName()) && cities.get(i).date == weatherRequest.getDt()){
                    return;
                }
            }
            storySource.addStory(new GetStoryData(weatherRequest).UpdateStory());
        }).start();
    }


    private void displayForecast(ForecastRequest forecastRequest1) { //отображаем прогноз, в зависимости от настроек (°С или °F)
        ArrayList<Forecast> forecasts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
        for (int i = 0; i < forecastRequest1.getDaily().length; i++) {
            Date date = new Date(forecastRequest1.getDaily()[i].getDt() * 1000);
            String dText = sdf.format(date);
            String dayTemp;
            String nightTemp;
            if (!metrics.isFahrenheit()) {
                dayTemp = String.format("%+.1f%s", forecastRequest1.getDaily()[i].getTemp().getDay() - 273.15f, "°C");
                nightTemp = String.format("%+.1f%s", forecastRequest1.getDaily()[i].getTemp().getNight() - 273.15f, "°C");
            }
            else {
                float dTemp = (forecastRequest1.getDaily()[i].getTemp().getDay()-273.15f)*1.8f+32;
                float nTemp = (forecastRequest1.getDaily()[i].getTemp().getNight()-273.15f)*1.8f+32;
                dayTemp = String.format("%+.1f%s", dTemp, "°F");
                nightTemp = String.format("%+.1f%s", nTemp, "°F");
            }
            String weatherIco = forecastRequest1.getDaily()[i].getWeather()[0].getIcon();
            Forecast forecast = new Forecast(dText, dayTemp, nightTemp, weatherIco);
            forecasts.add(forecast);
        }
        weatherAdapter.setDays(forecasts);

    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel("2", "Weather", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CURRENT_WEATHER", weatherRequest);
        outState.putSerializable("FORECAST", forecastRequest);
    }
    private void loadCurrentWeather(){
        CurrentWeather currentWeather = CurrentWeather.create(weatherRequest);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weatherFragment, currentWeather)
                .commit();
    }

}