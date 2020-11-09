package com.example.weatherforecast.model;

import android.util.Log;

import com.example.weatherforecast.BuildConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class Data {
    private static final String TAG = "WEATHER";
    private String cityName;


   public Data(String cityName){
       this.cityName = cityName;
   }


   public WeatherRequest getData() {
        HttpsURLConnection urlConnection = null;
        WeatherRequest weatherRequest = null;
        try {
            final String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", cityName, BuildConfig.WEATHER_API_KEY);
            final URL uri = new URL(url);
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
            String result = getLines(in);
            // преобразование данных запроса в модель
            Gson gson = new Gson();
            weatherRequest = gson.fromJson(result, WeatherRequest.class);
            // Возвращаемся к основному потоку
        } catch (Exception e) {
            Log.e(TAG, "Fail connection", e);
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return weatherRequest;
    }
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
}
