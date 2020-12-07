package com.example.weatherforecast.forecastModel;

import java.io.Serializable;

public class Daily implements Serializable {
    long dt;
    Temp temp;
    Weather[] weather;
    int pressure;
    int humidity;
    float wind_speed;
    float wind_deg;

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

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public float getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(float wind_deg) {
        this.wind_deg = wind_deg;
    }
}
