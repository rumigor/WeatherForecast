package com.example.weatherforecast.forecastModel;

import java.io.Serializable;

public class ForecastRequest implements Serializable {
    private float lat;
    private float lon;
    private Daily [] daily;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public void setDaily(Daily[] daily) {
        this.daily = daily;
    }
}
