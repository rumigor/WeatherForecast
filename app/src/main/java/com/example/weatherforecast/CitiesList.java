package com.example.weatherforecast;

public class CitiesList {
    private String city;
    private String temp;
    private String date;

    public CitiesList(String city, String temp, String date) {
        this.city = city;
        this.temp = temp;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
