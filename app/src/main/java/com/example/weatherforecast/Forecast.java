package com.example.weatherforecast;

import java.util.ArrayList;

public class Forecast {
    private String day;
    private String dTemp;
    private String nTemp;
    private String wCond;

    public Forecast(String day, String dTemp, String nTemp, String wCond) {
        this.day = day;
        this.dTemp = dTemp;
        this.nTemp = nTemp;
        this.wCond = wCond;
    }

    public String getDay() {
        return day;
    }

    public String getdTemp() {
        return dTemp;
    }

    public String getnTemp() {
        return nTemp;
    }

    public String getwCond() {
        return wCond;
    }
}
