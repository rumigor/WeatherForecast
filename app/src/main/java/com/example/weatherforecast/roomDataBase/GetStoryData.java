package com.example.weatherforecast.roomDataBase;

import com.example.weatherforecast.modelCurrentWeather.WeatherRequest;

public class GetStoryData {
    private WeatherRequest weatherRequest;

    public GetStoryData(WeatherRequest weatherRequest) {
        this.weatherRequest = weatherRequest;
    }

    public Story UpdateStory(){
        Story story = new Story();
        story.city = weatherRequest.getName();
        story.temperature = weatherRequest.getMain().getTemp();
        story.date = weatherRequest.getDt();
        return story;
    }



}
