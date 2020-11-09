package com.example.weatherforecast;

import java.util.ArrayList;

public class Cities {
    private static Cities instance;
    private ArrayList<String> citiesList;

    private Cities(ArrayList<String> citiesList) {
        this.citiesList = citiesList;
    }

    public static Cities getInstance(ArrayList<String> citiesList) {
        if (instance == null) {
            instance = new Cities(citiesList);
        }
        return instance;
    }

    public void setCitiesList(ArrayList<String> citiesList) {
        this.citiesList = citiesList;
    }

    public ArrayList<String> getCitiesList() {
        return citiesList;
    }
}
