package com.example.weatherforecast.roomDataBase;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;
    private StoryDatabase db;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(
                getApplicationContext(),
                StoryDatabase.class,
                "story_database")
                .allowMainThreadQueries()
                .build();
    }

    public StoryDao getStoryDao(){
        return db.getStoryDao();
    }
}
