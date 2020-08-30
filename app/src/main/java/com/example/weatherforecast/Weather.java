package com.example.weatherforecast;

import java.io.Serializable;

public class Weather implements Serializable {
    private String cityName;
    private String temperature;
    private int wCond;

    public Weather(String cityName, String temperature, int wCond) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.wCond = wCond;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getwCond() {
        return wCond;
    }
}
