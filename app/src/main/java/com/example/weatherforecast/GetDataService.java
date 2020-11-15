package com.example.weatherforecast;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.weatherforecast.forecast.ForecastRequest;
import com.example.weatherforecast.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetDataService extends IntentService {
    private static final String ACTION_LOAD_DATA = "LOAD_DATA";
    static final String CURRENT_WEATHER = "CURRENT_WEATHER";
    static final String FORECAST_DATA = "FORECAST_DATA";
    private CurrentWeatherRetrofit currentWeatherRetrofit;
    private ForecastRetrofit forecastRetrofit;
    private WeatherRequest weatherRequest;
    private ForecastRequest forecastRequest;


    private static final String CITY_NAME = "CITY_NAME";



    public GetDataService() {
        super("GetDataService");
    }


    public static void startGetDataService(Context context, String cityName) {
        Intent intent = new Intent(context, GetDataService.class);
        intent.setAction(ACTION_LOAD_DATA);
        intent.putExtra(CITY_NAME, cityName);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_DATA.equals(action)) {
                final String city = intent.getStringExtra(CITY_NAME);
                handleActionLoadData(city);
            }
        }
    }


    private void handleActionLoadData(String city) {
        initRetorfit();
        requestCurrentWeatherRetrofit(city, BuildConfig.WEATHER_API_KEY);
    }

    private void makeNote(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.drawable.title_small)
                .setContentTitle("Weather Forecast")
                .setContentText(message);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void sendBrodcast(WeatherRequest weatherRequest, ForecastRequest forecastRequest) { //отправляем данные клиенту
        Intent broadcastIntent = new Intent(CurrentWeatherFragment.BROADCAST_GET_DATA);
        broadcastIntent.putExtra(CURRENT_WEATHER, weatherRequest).putExtra(FORECAST_DATA, forecastRequest);
        sendBroadcast(broadcastIntent);
    }
    private void initRetorfit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currentWeatherRetrofit = retrofit.create(CurrentWeatherRetrofit.class);
        forecastRetrofit = retrofit.create(ForecastRetrofit.class);
    }


    private void requestCurrentWeatherRetrofit(String city, String keyApi){
        currentWeatherRetrofit.loadWeather(city, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            weatherRequest = response.body();
                            String exclude = "current,minutely,hourly,alerts";
                            requestForecastRetrofit(weatherRequest.getCoord().getLat(), weatherRequest.getCoord().getLon(), exclude);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        makeNote(getString(R.string.cityNotFound));
                        sendBrodcast(null, null);
                    }
                });
    }

    private void requestForecastRetrofit(float lat, float lon, String exclude){
        forecastRetrofit.loadWeather(lat, lon, exclude, BuildConfig.WEATHER_API_KEY)
                .enqueue(new Callback<ForecastRequest>() {
                    @Override
                    public void onResponse(Call<ForecastRequest> call, Response<ForecastRequest> response) {
                        if (response.body() != null) {
                            forecastRequest = response.body();
                            makeNote(weatherRequest.getName() + " " + ((int) weatherRequest.getMain().getTemp() - 273 + "°C"));
                            sendBrodcast(weatherRequest, forecastRequest);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastRequest> call, Throwable t) {
                        makeNote(getString(R.string.cityNotFound));
                        sendBrodcast(null, null);
                    }
                });
    }

}
