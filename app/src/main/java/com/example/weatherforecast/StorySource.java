package com.example.weatherforecast;

import com.example.weatherforecast.roomDataBase.Story;
import com.example.weatherforecast.roomDataBase.StoryDao;

import java.util.List;

public class StorySource {
    private final StoryDao storyDao;

    private List<Story> storyList;

    public StorySource(StoryDao storyDao){
        this.storyDao = storyDao;
    }

    public List<Story> getStoryList(){
        if (storyList == null){
            LoadStoryList();
        }
        return storyList;
    }

    public void LoadStoryList(){
        storyList = storyDao.getAllStories();
    }


    public long getCountStoryList(){
        return storyDao.getCountStories();
    }

    public void addStory(Story story){
        storyDao.insertStory(story);
        LoadStoryList();
    }

    public void updateStory(Story story){
        storyDao.updateStory(story);
        LoadStoryList();
    }

    public void removeStory(long id){
        storyDao.deteleStoryById(id);
        LoadStoryList();
    }

}
