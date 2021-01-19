package com.example.weatherforecast;

public class Metrics {
    private boolean fahrenheit;
    private static Metrics instance;

    public void setFahrenheit(boolean fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    private Metrics(boolean fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public boolean isFahrenheit() {
        return fahrenheit;
    }

    public static Metrics getInstance() {
        if (instance == null) {
            instance = new Metrics(false);
        }
        return instance;
    }
}
