package com.example.weatherforecast;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;



public class SearchHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    private String cityName;
     final CityAdapter cityAdapter = new CityAdapter();
    private Cities cities;
    private List<String> citiesNew = new ArrayList<String>();
    private static final String NIGHT_THEME = "darkTheme";

    CurrentWeatherFragment currentWeatherFragment;

    public SearchHistory() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> firstcities = new ArrayList<>();
        firstcities.addAll((Arrays.asList(getString(R.string.spb), getString(R.string.vln), getString(R.string.bcn), getString(R.string.msc), getString(R.string.bru))));
        cities = Cities.getInstance(firstcities);
        citiesNew.addAll(cities.getCitiesList());
        final RecyclerView recyclerView = view.findViewById(R.id.cityRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(cityAdapter);
        updateCities(citiesNew);
        cityAdapter.setOnCityClickListener(new CityAdapter.OnCityClickListener() {
            @Override
            public void onClicked(String city) {
                cityName = city;
                MainActivity.cityName = city;
                currentWeatherFragment = CurrentWeatherFragment.create(cityName);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragment, currentWeatherFragment)
                        .commit();
            }
        });
    }

    private void updateCities(List<String> cities) {
        cityAdapter.setCities(cities);
    }
}