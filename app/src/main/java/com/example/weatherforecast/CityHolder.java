package com.example.weatherforecast;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityHolder extends RecyclerView.ViewHolder {
    private final TextView cityName;
    private final TextView lastTemp;
    private final TextView lastDate;

    public CityHolder(@NonNull View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.cityName);
        lastTemp = itemView.findViewById(R.id.lastTemp);
        lastDate = itemView.findViewById(R.id.lastDate);
    }

    public TextView getCityName() {
        return cityName;
    }

    void bind(final CitiesList cities, final CityAdapter.OnCityClickListener onCityClickListener) {
        cityName.setText(cities.getCity());
        lastTemp.setText(cities.getTemp());
        lastDate.setText(cities.getDate());
         cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityClickListener != null) {
                    onCityClickListener.onClicked(cities.getCity());
                }
            }
        });
    }
}
