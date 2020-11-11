package com.example.weatherforecast.forecast;

import java.io.Serializable;

public class Daily implements Serializable {
    long dt;
    Temp temp;
    Weather[] weather;

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long time) {
        this.dt = time;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }
}
