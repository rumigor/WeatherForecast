package com.example.weatherforecast;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.weatherforecast.forecast.ForecastRequest;
import com.example.weatherforecast.model.WeatherRequest;
import com.google.gson.Gson;

public class GetDataService extends IntentService {
    private static final String ACTION_LOAD_DATA = "LOAD_DATA";
    static final String CURRENT_WEATHER = "CURRENT_WEATHER";
    static final String FORECAST_DATA = "FORECAST_DATA";

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
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, BuildConfig.WEATHER_API_KEY);
        GetData getData = new GetData(url);
        if (!getData.loadData().equals("error")) {
            Gson gson = new Gson();
            weatherRequest = gson.fromJson(getData.loadData(), WeatherRequest.class);
            if (weatherRequest != null) {
                url = String.format("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=current,minutely,hourly,alerts&appid=%s", weatherRequest.getCoord().getLat(), weatherRequest.getCoord().getLon(), BuildConfig.WEATHER_API_KEY);
                getData = new GetData(url);
                forecastRequest = gson.fromJson(getData.loadData(), ForecastRequest.class);
                makeNote(city + " " + ((int) weatherRequest.getMain().getTemp() - 273 + "°C"));
                sendBrodcast(weatherRequest, forecastRequest);
            }
        }
        else {
            makeNote(getString(R.string.cityNotFound));
            sendBrodcast(weatherRequest, forecastRequest);
        }



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

    private void sendBrodcast(WeatherRequest weatherRequest, ForecastRequest forecastRequest) {
        Intent broadcastIntent = new Intent(CurrentWeatherFragment.BROADCAST_GET_DATA);
        broadcastIntent.putExtra(CURRENT_WEATHER, weatherRequest).putExtra(FORECAST_DATA, forecastRequest);
        sendBroadcast(broadcastIntent);
    }


}
