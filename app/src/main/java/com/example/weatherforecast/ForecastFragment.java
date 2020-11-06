package com.example.weatherforecast;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherforecast.forecast.ForecastData;
import com.example.weatherforecast.forecast.ForecastRequest;
import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class ForecastFragment extends Fragment {

    private static final String CITY_NAME = "CityName";

    private String cityName;
    private TextView day;
    private  TextView temperature;
    private ImageView weatherCondition;
    private WeatherRequest weatherRequest;
    private

    final WeatherAdapter weatherAdapter = new WeatherAdapter();


//    private final Forecast[] spbDays = {new Forecast("Monday", "+18°C", 2), new Forecast("Tuesday", "+19°C", 1),
//            new Forecast("Wednesday", "+16°", 4), new Forecast("Thursday", "+19°C", 1), new Forecast("Friday", "+18°C", 2), new Forecast("Saturday", "+14°C", 4),
//            new Forecast("Sunday", "+17°C", 3)};
//
//
//    private final Forecast[] vlnDays = {new Forecast("Monday", "+20°C", 1), new Forecast("Tuesday", "+22°C", 5),
//            new Forecast("Wednesday", "+16°", 4), new Forecast("Thursday", "+21°C", 2), new Forecast("Friday", "+18°C", 4), new Forecast("Saturday", "+17°C", 3),
//            new Forecast("Sunday", "+17°C", 3)};
//
//    private final Forecast[] bcnDays = {new Forecast("Monday", "+28°C", 2), new Forecast("Tuesday", "+31°C", 1),
//            new Forecast("Wednesday", "+26°", 3), new Forecast("Thursday", "+27°C", 2), new Forecast("Friday", "+28°C", 1), new Forecast("Saturday", "+23°C", 4),
//            new Forecast("Sunday", "+22°C", 3)};
//
//    private final Forecast[] mscDays = {new Forecast("Monday", "+28°C", 2), new Forecast("Tuesday", "+22°C", 5),
//            new Forecast("Wednesday", "+20°", 1), new Forecast("Thursday", "+21°C", 1), new Forecast("Friday", "+22°C", 2), new Forecast("Saturday", "+17°C", 4),
//            new Forecast("Sunday", "+22°C", 2)};
//
//    private final Forecast[] bruDays = {new Forecast("Monday", "+24°C", 4), new Forecast("Tuesday", "+18°C", 4),
//            new Forecast("Wednesday", "+19°", 2), new Forecast("Thursday", "+20°C", 3), new Forecast("Friday", "+24°C", 3), new Forecast("Saturday", "+18°C", 4),
//            new Forecast("Sunday", "+19°C", 2)};
//    private final Forecast[] noData = {new Forecast("Monday", "-°C", 1), new Forecast("Tuesday", "-°C", 1),
//            new Forecast("Wednesday", "-°C", 1), new Forecast("Thursday", "-°C", 1), new Forecast("Friday", "-°C", 1), new Forecast("Saturday", "-°C", 1),
//            new Forecast("Sunday", "-°C", 1)};

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
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),  LinearLayoutManager.HORIZONTAL);
            itemDecoration.setDrawable(requireActivity().getDrawable(R.drawable.separator));
            recyclerView.addItemDecoration(itemDecoration);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),  LinearLayoutManager.VERTICAL);
            itemDecoration.setDrawable(requireActivity().getDrawable(R.drawable.separator));
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.setAdapter(weatherAdapter);


        if (savedInstanceState != null){
            cityName = savedInstanceState.getString(CITY_NAME);
        }

        if (cityName == null){
            cityName = "Saint Petersburg,RU";
        }
        dataLoading(cityName);
//        if (cityName.equals(getString(R.string.spb))){
//            weatherAdapter.setDays(Arrays.asList(spbDays));
//        }
//        else if (cityName.equals(getString(R.string.vln))){
//            weatherAdapter.setDays(Arrays.asList(vlnDays));
//        }
//        else if (cityName.equals(getString(R.string.bcn))){
//            weatherAdapter.setDays(Arrays.asList(bcnDays));
//        }
//        else if (cityName.equals(getString(R.string.msc))){
//            weatherAdapter.setDays(Arrays.asList(mscDays));
//        }
//        else if (cityName.equals(getString(R.string.bru))){
//            weatherAdapter.setDays(Arrays.asList(bruDays));
//        }
//        else weatherAdapter.setDays(Arrays.asList(noData));
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CITY_NAME, cityName);
    }

    private void dataLoading(String cityName){
        final Handler handler = new Handler();
        final Data data = new Data(cityName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WeatherRequest weatherRequest = data.getData();
                final ForecastData forecastData = new ForecastData(weatherRequest.getCoord().getLat(), weatherRequest.getCoord().getLon());
                final ForecastRequest forecastRequest = forecastData.getData();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            displayWeather(forecastRequest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    private void displayWeather(ForecastRequest forecastRequest1) throws IOException {
                        ArrayList<Forecast> forecasts = new ArrayList<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MM", Locale.getDefault());
                        for (int i = 0; i < forecastRequest1.getDaily().length; i++) {
                            Date date = new Date(forecastRequest1.getDaily()[i].getDt());
                            String dText = sdf.format(date);
                            String dayTemp = String.format("%d%s", (int)(forecastRequest1.getDaily()[i].getTemp().getDay() - 273), "°C");
                            String nightTemp = String.format("%d%s", (int)(forecastRequest1.getDaily()[i].getTemp().getNight() - 273), "°C");
                            String weatherIco = forecastRequest1.getDaily()[i].getWeather()[0].getIcon();
                            Forecast forecast = new Forecast(dText, dayTemp, nightTemp, weatherIco);
                            forecasts.add(forecast);
                        }
                        weatherAdapter.setDays(forecasts);
                    }
                });
            }
        }).start();
    }


}