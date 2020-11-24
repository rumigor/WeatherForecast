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
    private List<Story> cities;
    private long menuPosition;
    private long citiesNumber;


    public CityAdapter(List<Story> cities, long citiesNumber) {
        this.cities = cities;
        this.citiesNumber = citiesNumber;
    }

    onLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(onLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }

    public void setCitiesNumber(long citiesNumber) {
        this.citiesNumber = citiesNumber;
    }

    public interface onLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }


    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    public void setCities(List<Story> cities) {
        this.cities = cities;
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
        Story story = cities.get(position);
        menuPosition = position;
        holder.bind(story, onCityClickListener);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.ItemLongClicked(v, position);
                }

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return (int)citiesNumber;
    }
    public long getMenuPosition() {
        return menuPosition;
    }

    interface OnCityClickListener {

        void onClicked(String city);
    }

}
