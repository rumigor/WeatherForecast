package com.example.weatherforecast.roomDataBase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStory(Story story);

    @Update
    void updateStory(Story story);

    @Delete
    void deleteStory(Story story);

    @Query("DELETE FROM story WHERE id = :id")
    void deteleStoryById(long id);

    @Query("SELECT * FROM story")
    List<Story> getAllStories();

    @Query("SELECT * FROM story WHERE id = :id")
    Story getStoryById(long id);

    @Query("SELECT COUNT() FROM story")
    long getCountStories();
}
