package com.example.weatherforecast.roomDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"city", "temperature", "date"})})

public class Story {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String city;

    @ColumnInfo
    public float temperature;

    @ColumnInfo
    public long date;
}
