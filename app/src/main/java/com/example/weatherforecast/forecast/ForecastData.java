package com.example.weatherforecast.forecast;

import android.util.Log;

import com.example.weatherforecast.BuildConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class ForecastData implements Serializable {
    private static final String TAG = "WEATHER";
    private float lat;
    private float lon;

    public ForecastData(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public ForecastRequest getData() {
        HttpsURLConnection urlConnection = null;
        ForecastRequest forecastRequest = null;
        try {
            final String url = String.format("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=current,minutely,hourly,alerts&appid=%s", lat, lon, BuildConfig.WEATHER_API_KEY);
            final URL uri = new URL(url);
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
            String result = getLines(in);
            // преобразование данных запроса в модель
            Gson gson = new Gson();
            forecastRequest = gson.fromJson(result, ForecastRequest.class);
            // Возвращаемся к основному потоку
        } catch (Exception e) {
            Log.e(TAG, "Fail connection", e);
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return forecastRequest;
    }
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
}
