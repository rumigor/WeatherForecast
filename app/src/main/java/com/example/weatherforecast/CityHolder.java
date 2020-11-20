package com.example.weatherforecast;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.roomDataBase.Story;

import java.text.SimpleDateFormat;
import java.util.Date;

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


    void bind(final Story cities, final CityAdapter.OnCityClickListener onCityClickListener) {
        cityName.setText(cities.city);
        lastTemp.setText((int)cities.temperature-273+"°C");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(cities.date*1000);
        lastDate.setText(sdf.format(date));
         cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityClickListener != null) {
                    onCityClickListener.onClicked(cities.city);
                }
            }
        });
    }
}
