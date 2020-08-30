package com.example.weatherforecast;

import java.util.ArrayList;

public class Forecast {
    private String day;
    private String temp;
    private int wCond;

    public Forecast(String day, String temp, int wCond) {
        this.day = day;
        this.temp = temp;
        this.wCond = wCond;
    }

    public String getDay() {
        return day;
    }

    public String getTemp() {
        return temp;
    }

    public int getwCond() {
        return wCond;
    }
}
