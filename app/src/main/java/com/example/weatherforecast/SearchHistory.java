package com.example.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherforecast.roomDataBase.App;
import com.example.weatherforecast.roomDataBase.StoryDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchHistory extends Fragment {

    private final static String CITY_NAME = "CityName";
    private String cityName;
    private CityAdapter cityAdapter;
    private CitiesList cities;
    private List<CitiesList> citiesNew = new ArrayList<CitiesList>();
    private StorySource storySource;
    private static final String NIGHT_THEME = "darkTheme";

    CurrentWeatherFragment currentWeatherFragment;

    public SearchHistory() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        updateCities(citiesNew);
        cityAdapter.setOnCityClickListener(city -> {
            cityName = city;
            MainActivity.cityName = city;
            SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CITY_NAME, cityName).commit();
            currentWeatherFragment = CurrentWeatherFragment.create(cityName);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, currentWeatherFragment)
                    .commit();
        });
    }
    private void initRecyclerView(View view){
        final RecyclerView recyclerView = view.findViewById(R.id.cityRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        StoryDao storyDao = App
                .getInstance()
                .getStoryDao();
        storySource = new StorySource(storyDao);
        cityAdapter = new CityAdapter(requireActivity(), storySource);
        recyclerView.setAdapter(cityAdapter);
    }
    private void updateCities(List<CitiesList> cities) {
        cityAdapter.setCities(cities);
    }
}