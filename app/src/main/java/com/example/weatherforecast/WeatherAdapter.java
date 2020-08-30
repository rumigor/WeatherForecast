package com.example.weatherforecast;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<ForecastHolder> {
    private List<Forecast> days;

    public void setDays(List<Forecast> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ForecastHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item, parent, false)
            );
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        holder.bind(days.get(position).getDay(), days.get(position).getTemp(), days.get(position).getwCond());
    }

    @Override
    public int getItemCount() {
        if (days == null) return 0;

        return days.size();
    }


}
