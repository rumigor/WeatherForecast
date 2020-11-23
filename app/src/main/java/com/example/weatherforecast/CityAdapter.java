package com.example.weatherforecast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.roomDataBase.Story;

import java.util.Collections;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private OnCityClickListener onCityClickListener;
    private Activity activity;
    private StorySource storySource;
    private long menuPosition;

    public CityAdapter(Activity activity, StorySource storySource) {
        this.activity = activity;
        this.storySource = storySource;
    }


    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CityHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cities_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        List<Story> citiesList = storySource.getStoryList();
//        Collections.reverse(citiesList);
        Story story = citiesList.get(position);
        holder.bind(story, onCityClickListener, position, activity);
        // Тут определим, в каком пункте меню было нажато
//        holder.itemView.setOnLongClickListener(view -> {
//            menuPosition = position;
//            return false;
//        });

        // регистрируем контекстное меню


    }

    @Override
    public int getItemCount() {
        return (int)storySource.getCountStoryList();
    }
    public long getMenuPosition() {
        return menuPosition;
    }

    interface OnCityClickListener {

        void onClicked(String city);
    }


}
