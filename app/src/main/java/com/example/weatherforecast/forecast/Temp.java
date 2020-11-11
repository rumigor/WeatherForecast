package com.example.weatherforecast.forecast;

import java.io.Serializable;

public class Temp implements Serializable {
    private float day;
    private float night;

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }
}
