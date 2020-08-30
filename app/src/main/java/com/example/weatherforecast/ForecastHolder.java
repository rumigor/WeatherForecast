package com.example.weatherforecast;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastHolder extends RecyclerView.ViewHolder{
    private final TextView day;
    private final TextView temperature;
    private final ImageView weatherCondition;


    public ForecastHolder(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.day);
        temperature = itemView.findViewById(R.id.temp);
        weatherCondition = itemView.findViewById(R.id.imageView);
    }

    public TextView getDay() {
        return day;
    }

    public TextView getTemperature() {
        return temperature;
    }

    public ImageView getWeatherCondition() {
        return weatherCondition;
    }

    public void bind (final String dayW, final String temperatureW, final int weatherConditionW){
        day.setText(dayW);
        temperature.setText(temperatureW);
        setPic(weatherConditionW);

    }

    private void setPic(int wCond) {
        switch (wCond){
            case 1:
                weatherCondition.setImageResource(R.drawable.sun2);
                break;
            case 2:
                weatherCondition.setImageResource(R.drawable.party_cloudy);
                break;
            case 3:
                weatherCondition.setImageResource(R.drawable.cloudy);
                break;
            case 4:
                weatherCondition.setImageResource(R.drawable.rainy);
                break;
            case 5:
                weatherCondition.setImageResource(R.drawable.thunderstorm);
                break;
        }
    }
}
