package com.example.weatherforecast.cityRecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherforecast.R;
import com.example.weatherforecast.roomDataBase.Story;
import com.example.weatherforecast.roomDataBase.StorySource;

import java.util.List;

public class FilteredCityAdapter extends RecyclerView.Adapter<FilteredCityHolder> {
    private FilteredCityAdapter.OnCityClickListener onCityClickListener;
    private StorySource storySource;
    private String cityName;



    public FilteredCityAdapter(StorySource storySource, String cityName) {
        this.storySource = storySource;
        this.cityName = cityName;
    }

    public FilteredCityAdapter.onLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(FilteredCityAdapter.onLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }


    public interface onLongItemClickListener {
        void ItemLongClicked(View v, int position);
    }


    public void setOnCityClickListener(FilteredCityAdapter.OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }


    @NonNull
    @Override
    public FilteredCityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FilteredCityHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cities_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredCityHolder holder, int position) {
        List<Story> cities = storySource.filterStoryByCityName(cityName);
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
        return (int) storySource.getFilteredStoryCount(cityName);
    }


    public interface OnCityClickListener {

        void onClicked(String city);
    }
}
