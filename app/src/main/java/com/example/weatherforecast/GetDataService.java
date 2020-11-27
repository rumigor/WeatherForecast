package com.example.weatherforecast;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.weatherforecast.forecastModel.ForecastRequest;
import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;
import com.example.weatherforecast.retrofit.CurrentWeatherRetrofit;
import com.example.weatherforecast.retrofit.ForecastRetrofit;
import com.example.weatherforecast.retrofit.WeatherRetrofitByCoords;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetDataService extends IntentService {
    private static final String ACTION_LOAD_DATA = "LOAD_DATA";
    static final String CURRENT_WEATHER = "CURRENT_WEATHER";
    static final String FORECAST_DATA = "FORECAST_DATA";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private CurrentWeatherRetrofit currentWeatherRetrofit;
    private ForecastRetrofit forecastRetrofit;
    private WeatherRetrofitByCoords weatherRetrofitByCoords;
    private WeatherRequest weatherRequest;
    private ForecastRequest forecastRequest;
    private float lat;
    private float lon;


    private static final String CITY_NAME = "CITY_NAME";



    public GetDataService() {
        super("GetDataService");
    }


    public static void startGetDataService(Context context, String cityName, float lat, float lon) {
        Intent intent = new Intent(context, GetDataService.class);
        intent.setAction(ACTION_LOAD_DATA);
        intent.putExtra(CITY_NAME, cityName);
        intent.putExtra(LATITUDE, lat);
        intent.putExtra(LONGITUDE, lon);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_DATA.equals(action)) {
                final String city = intent.getStringExtra(CITY_NAME);
                lat = intent.getFloatExtra(LATITUDE, lat);
                lon = intent.getFloatExtra(LONGITUDE, lon);
                handleActionLoadData(city, lat, lon);
            }
        }
    }


    private void handleActionLoadData(String city, float lat, float lon) {
        initRetorfit();
        requestCurrentWeatherRetrofit(city, lat, lon, BuildConfig.WEATHER_API_KEY);
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
        weatherRetrofitByCoords = retrofit.create(WeatherRetrofitByCoords.class);
    }


    private void requestCurrentWeatherRetrofit(String city, float lat, float lon, String keyApi){
        String lang;
        String language = Locale.getDefault().getDisplayLanguage(); //получаем текущий язык системы
        if (language.equals("русский")){lang = "ru";} //если выбран русский язык, то будем получать данные о погоде на русском языке
        else {lang = "en";} //иначе на английском
        if (lat == 0 || lon ==0) {
            currentWeatherRetrofit.loadWeather(city, keyApi, lang)
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
        } else {
            weatherRetrofitByCoords.loadWeather(lat, lon, keyApi, lang)
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
