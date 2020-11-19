package com.example.weatherforecast.roomDataBase;

import androidx.room.Index;

@Entity(indices = {@Index(value = {"first_name", "last_name"})})

public class Entity {
}
