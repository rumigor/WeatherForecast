package com.example.weatherforecast;

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
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherforecast.cityRecycleView.CityAdapter;
import com.example.weatherforecast.roomDataBase.App;
import com.example.weatherforecast.roomDataBase.Story;
import com.example.weatherforecast.roomDataBase.StoryDao;
import com.example.weatherforecast.roomDataBase.StorySource;

import java.util.List;


public class SearchHistory extends Fragment {

    private final static String CITY_NAME = "CityName";
    private String cityName;
    private CityAdapter cityAdapter;
    private StorySource storySource;
    private List<Story> cities;
    private RecyclerView recyclerView;
    private int mCurrentItemPosition;
    private TextView cityFilter;
    private Button filter;

    private CurrentWeatherFragment currentWeatherFragment;

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
        registerForContextMenu(recyclerView);
        clicks();
        cityFilter = view.findViewById(R.id.cityFilter);
        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(v -> {
            cities = storySource.filterStoryByCityName(cityFilter.getText().toString());
            cityAdapter = new CityAdapter(storySource);
            recyclerView.setAdapter(cityAdapter);
            registerForContextMenu(recyclerView);
            clicks();
        });

    }
    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.cityRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        StoryDao storyDao = App
                .getInstance()
                .getStoryDao();
        storySource = new StorySource(storyDao);
        cities = storySource.getStoryList();
        cityAdapter = new CityAdapter(storySource);
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
                Story story = storySource
                        .getStoryList()
                        .get(mCurrentItemPosition);
                weatherFrameLoading(story.city);
                return true;
            case R.id.remove_context:
                Story storyForRemove = storySource
                        .getStoryList()
                        .get(mCurrentItemPosition);
                storySource.removeStory(storyForRemove.id);
                cityAdapter.notifyItemRemoved(mCurrentItemPosition);
                cityAdapter.notifyDataSetChanged();
                return true;
            case R.id.clear_context:
                for (int i = 0; i < cities.size(); i++) {
                    storySource.removeStory(cities.get(i).id);
                }
                cityAdapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }
    private void weatherFrameLoading(String city){
        cityName = city;
        MainActivity.cityName = city;
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITY_NAME, cityName).commit();
        currentWeatherFragment = CurrentWeatherFragment.create(cityName, (float) 0, (float) 0);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, currentWeatherFragment)
                .commit();
    }

    private void clicks(){
        cityAdapter.setOnCityClickListener(city -> weatherFrameLoading(city));
        cityAdapter.setOnLongItemClickListener((v, position) -> {
            mCurrentItemPosition = position;
            v.showContextMenu();
        });
    }
}