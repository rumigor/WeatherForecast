package com.example.weatherforecast.retrofit;

import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrentWeatherRetrofit {
        @GET("data/2.5/weather")
        Call<WeatherRequest> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi, @Query("lang") String language);
}
