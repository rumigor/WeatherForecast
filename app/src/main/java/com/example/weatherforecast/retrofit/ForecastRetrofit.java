package com.example.weatherforecast.retrofit;

import com.example.weatherforecast.forecastModel.ForecastRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastRetrofit {
        @GET("data/2.5/onecall")
        Call<ForecastRequest> loadWeather(@Query("lat") float lat, @Query("lon") float lon, @Query("exclude") String exclude, @Query("appid") String keyApi);
}
