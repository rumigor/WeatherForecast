package com.example.weatherforecast;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private List<CitiesList> cities;
    private OnCityClickListener onCityClickListener;

    public void setCities(List<CitiesList> cities) {
        this.cities = cities;
        notifyDataSetChanged();
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
        holder.bind(cities.get(position), onCityClickListener);
    }

    @Override
    public int getItemCount() {
        if (cities == null) return 0;

        return cities.size();
    }
    interface OnCityClickListener {

        void onClicked(String city);
    }
}
