package com.example.weatherforecast;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.model.Data;
import com.example.weatherforecast.model.WeatherRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String CITY_NAME ="CityName";
    private String cityName;
    Forecast forecast;
    final CityAdapter cityAdapter = new CityAdapter();
    TextInputEditText search;
    final ArrayList<String> cities = new ArrayList<String>();
    final ArrayList<String> citiesNew = new ArrayList<String>();
    private static final String NIGHT_THEME = "darkTheme";
    private boolean nightMode;
    ConstraintLayout layout;
    Pattern checkCityName = Pattern.compile("^[A-Z][a-z]*$");
    CurrentWeatherFragment currentWeatherFragment;

    public SearchHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchHistory newInstance(String param1, String param2) {
        SearchHistory fragment = new SearchHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cities.addAll((Arrays.asList(getString(R.string.spb), getString(R.string.vln), getString(R.string.bcn), getString(R.string.msc), getString(R.string.bru))));
        citiesNew.addAll(cities);
        final RecyclerView recyclerView = view.findViewById(R.id.cityRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(cityAdapter);
        updateCities(cities);
        cityAdapter.setOnCityClickListener(new CityAdapter.OnCityClickListener() {
            @Override
            public void onClicked(String city) {
                if (city.equals(cities.get(0))) {
                    cityName = "Saint Petersburg,RU";
                }
                else if (city.equals(cities.get(1))){
                    cityName = "Vilnius,LT";
                }
                else if (city.equals(cities.get(2))){
                    cityName = "Barcelona,ES";
                }
                else if (city.equals(cities.get(3))){
                    cityName = "Moscow";
                }
                else if (city.equals(cities.get(4))){
                    cityName = "Brussels,BE";
                }
                else {
                    cityName = city;
                }
//                currentWeatherFragment = (CurrentWeatherFragment)getSupportFragmentManager().findFragmentById(R.id.currentWeather);
//                currentWeatherFragment = CurrentWeatherFragment.create(cityName);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.currentWeather, currentWeatherFragment)
//                        .commit();
//                intent.putExtra(CITY_NAME, cityName);
//                setResult(RESULT_OK, intent);
//                finish();
            }

        });

        search = view.findViewById(R.id.cityType);

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Как только фокус потерян, сразу проверяем на валидность данные
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                // Это сама валидация, она вынесена в отдельный метод, чтобы не дублировать код
                // см вызов ниже
                validate(tv, checkCityName, "Неправильное название города!");
            }
        });

        final Button addCity = view.findViewById(R.id.searchButton);
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search.getText() != null) {
                    cityName = search.getText().toString();
                    final Handler handler = new Handler();
                    final Data data = new Data(cityName);
                    final WeatherRequest[] weatherRequest = new WeatherRequest[1];
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            weatherRequest[0] = data.getData();
                        }
                    }).start();
                    if (weatherRequest[0] == null) {
                        Toast.makeText(getContext(), R.string.cityNotFound, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    citiesNew.add(search.getText().toString());
                    cityAdapter.setCities(citiesNew);
                    search.setText("");
                }
            }
        });
        final Button remove = view.findViewById(R.id.resetButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCities(cities);
            }
        });
    }
    private void validate(TextView tv, Pattern checkCityName, String s) {
        String value = tv.getText().toString();
        if (checkCityName.matcher(value).matches()){    // Проверим на основе регулярных выражений
            hideError(tv);
        }
        else{
            showError(tv, s);
        }



    }

    // Показать ошибку
    private void showError(TextView view, String message) {
        view.setError(message);
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
    }


    private void updateCities(List<String> cities) {
        cityAdapter.setCities(cities);
    }

    private void removeCities(List<String> removeCities){
        cityAdapter.setCities(removeCities);
        citiesNew.clear();
        citiesNew.addAll(cities);
        cityAdapter.notifyDataSetChanged();
    }

}