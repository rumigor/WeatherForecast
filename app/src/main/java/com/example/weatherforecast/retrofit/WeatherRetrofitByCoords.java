package com.example.weatherforecast.retrofit;

import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRetrofitByCoords {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("lat") float lat, @Query("lon") float lon, @Query("appid") String keyApi, @Query("lang") String language);
}
