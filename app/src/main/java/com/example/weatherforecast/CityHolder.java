package com.example.weatherforecast;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityHolder extends RecyclerView.ViewHolder {
    private final TextView cityName;

    public CityHolder(@NonNull View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.cityName);
    }

    public TextView getCityName() {
        return cityName;
    }

    void bind(final String city, final CityAdapter.OnCityClickListener onCityClickListener) {
        cityName.setText(city);
         cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityClickListener != null) {
                    onCityClickListener.onClicked(city);
                }
            }
        });
    }
}
