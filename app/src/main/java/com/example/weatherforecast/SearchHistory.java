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

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherforecast.roomDataBase.App;
import com.example.weatherforecast.roomDataBase.Story;
import com.example.weatherforecast.roomDataBase.StoryDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchHistory extends Fragment {

    private final static String CITY_NAME = "CityName";
    private String cityName;
    private CityAdapter cityAdapter;
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
        cityAdapter.setOnCityClickListener(this::weatherFrameLoading);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update_context:
                // Изменение имени и фамилии у студента
                Story story = storySource
                        .getStoryList()
                        .get((int) cityAdapter.getMenuPosition());
                weatherFrameLoading(story.city);
                return true;
            case R.id.remove_context:
                // Удалить запись из базы
                Story storyForRemove = storySource
                        .getStoryList()
                        .get((int) cityAdapter.getMenuPosition());
                storySource.removeStory(storyForRemove.id);
                cityAdapter.notifyItemRemoved((int) cityAdapter.getMenuPosition());
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void weatherFrameLoading(String city){
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
    }
}