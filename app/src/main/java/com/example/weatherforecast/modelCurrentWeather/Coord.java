package com.example.weatherforecast.modelCurrentWeather;

import java.io.Serializable;

public class Coord implements Serializable {
    private float lat;
    private float lon;

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
}
