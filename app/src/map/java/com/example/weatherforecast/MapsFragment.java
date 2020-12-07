package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback{
    private static final int PERMISSION_REQUEST_CODE = 10;

    private EditText textLatitude;
    private EditText textLongitude;
    private TextView textAddress;
    private Marker currentMarker;
    private GoogleMap mMap;
    private float lat;
    private float lon;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng = new LatLng(-34, 151);
        lat = (float)latLng.latitude;
        lon = (float)latLng.longitude;
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Текущая позиция"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                getAddress(latLng);
                currentMarker.setPosition(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                textLatitude.setText(new DecimalFormat("#0.000000").format(latLng.latitude));
                textLongitude.setText(new DecimalFormat("#0.000000").format(latLng.longitude));
                lat = (float) latLng.latitude;
                lon = (float) latLng.longitude;
            }
        });
    }

    private void getAddress(final LatLng location){
        final Geocoder geocoder = new Geocoder(getContext());
        // Поскольку Geocoder работает по интернету, создаём отдельный поток
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    Log.d("GEOCODER", String.valueOf(addresses.size()));
                    textAddress.post(new Runnable() {
                        @Override
                        public void run() {
                            textAddress.setText(addresses.get(0).getAddressLine(0));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        initViews(view);
        requestPemissions();
    }
    private void initViews(View view) {
        textLatitude = view.findViewById(R.id.editLat);
        textLongitude = view.findViewById(R.id.editLng);
        textAddress = view.findViewById(R.id.textAddress);
        initSearchByAddress(view);
        Button viewWeather = view.findViewById(R.id.viewWeather);
        viewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherFragment weatherFragment = WeatherFragment.create(null, lat, lon);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragment, weatherFragment)
                        .addToBackStack("WEATHER_FRAGMENT")
                        .commit();
            }
        });
    }
    private void requestPemissions() {
        // Проверим, есть ли Permission’ы, и если их нет, запрашиваем их у
        // пользователя
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем координаты
            requestLocation();
        } else {
            // Permission’ов нет, запрашиваем их у пользователя
            requestLocationPermissions();
        }
    }
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        // Получаем менеджер геолокаций
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // Получаем наиболее подходящий провайдер геолокации по критериям.
        // Но определить, какой провайдер использовать, можно и самостоятельно.
        // В основном используются LocationManager.GPS_PROVIDER или
        // LocationManager.NETWORK_PROVIDER, но можно использовать и
        // LocationManager.PASSIVE_PROVIDER - для получения координат в
        // пассивном режиме
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 10 секунд или каждые
            // 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latd = location.getLatitude(); // Широта
                    String latitude = Double.toString(latd);
                    textLatitude.setText(latitude);

                    double lng = location.getLongitude(); // Долгота
                    String longitude = Double.toString(lng);
                    textLongitude.setText(longitude);
                    lat = (float) latd;
                    lon = (float) lng;
                    String accuracy = Float.toString(location.getAccuracy());   // Точность

                    LatLng currentPosition = new LatLng(latd, lng);
                    currentMarker.setPosition(currentPosition);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float)12));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
        if (lat == 0 && lon ==0){
            provider = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(provider, 10000, 1000, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double latd = location.getLatitude(); // Широта
                    String latitude = Double.toString(latd);
                    textLatitude.setText(latitude);

                    double lng = location.getLongitude(); // Долгота
                    String longitude = Double.toString(lng);
                    textLongitude.setText(longitude);
                    lat = (float) latd;
                    lon = (float) lng;
                    String accuracy = Float.toString(location.getAccuracy());   // Точность

                    LatLng currentPosition = new LatLng(latd, lng);
                    currentMarker.setPosition(currentPosition);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float)12));
                }
            });
        }
    }

    // Запрашиваем Permission’ы для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
            // Запрашиваем эти два Permission’а у пользователя
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }


    // Результат запроса Permission’а у пользователя:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {   // Запрошенный нами
            // Permission
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                // Запросим координаты
                requestLocation();
            }
        }
    }
    private void initSearchByAddress(View view) {
        final EditText textSearch = view.findViewById(R.id.searchAddress);
        view.findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(),0);
                final Geocoder geocoder = new Geocoder(requireActivity());
                final String searchText = textSearch.getText().toString();
                // Операция получения занимает некоторое время и работает по
                // интернету. Поэтому её необходимо запускать в отдельном потоке
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Получаем координаты по адресу
                            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);
                            if (addresses.size() > 0){
                                final LatLng location = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentMarker.setPosition(location);
                                        lat = (float)location.latitude;
                                        lon = (float)location.longitude;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, (float)15));
                                        textLatitude.setText(new DecimalFormat("#0.000000").format(lat));
                                        textLongitude.setText(new DecimalFormat("#0.000000").format(lon));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }



}