package com.example.weatherforecast.cityRecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.roomDataBase.StorySource;
import com.example.weatherforecast.roomDataBase.Story;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private OnCityClickListener onCityClickListener;
    private StorySource storySource;



    public CityAdapter(StorySource storySource) {
        this.storySource = storySource;
    }

    public onLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(onLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }


    public interface onLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }


    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }


       @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CityHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cities_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        List<Story> cities = storySource.getStoryList();
        Story story = cities.get(position);
        holder.bind(story, onCityClickListener);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.ItemLongClicked(v, position);
                }

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return (int) storySource.getCountStoryList();
    }


    public interface OnCityClickListener {

        void onClicked(String city);
    }

}
