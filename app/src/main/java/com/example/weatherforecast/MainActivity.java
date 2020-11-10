package com.example.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.weatherforecast.model.OnDialogListener;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final static String CITY_NAME = "CityName";
    public static String cityName;

    CurrentWeatherFragment currentWeatherFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initMain(savedInstanceState);

    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }



    private void initMain(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentWeatherFragment = (CurrentWeatherFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mainFragment);


        } else if (currentWeatherFragment == null ) {
            currentWeatherFragment = new CurrentWeatherFragment();

        }
        if (!currentWeatherFragment.isInLayout()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, currentWeatherFragment)
                    .commit();
        }

        final String city;
        if (cityName == null){
            city = "Saint Petersburg,RU";
        }
        else city = cityName;
        Button goToWeb = findViewById(R.id.openInternet);
        goToWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="";
                if (city.equals(getString(R.string.spb))){
                    url = "https://www.gismeteo.ru/weather-sankt-peterburg-4079/";
                }
                else if (city.equals(getString(R.string.vln))){
                    url = "https://www.gismeteo.ru/weather-vilnius-4230/";
                }
                else if (city.equals(getString(R.string.bcn))){
                    url = "https://www.gismeteo.ru/weather-barcelona-1948/";
                }
                else url = "https://www.gismeteo.ru/";
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        final SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName = query;
                searchText.onActionViewCollapsed();
                Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_LONG).show();

                fragmentLoading();
                return true;
            }
            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (cityName == null) {cityName = "Saint Petersburg,RU";}
                fragmentLoading();
                return true;
            }
        });
        MenuItem tools = menu.findItem(R.id.action_settings);
        tools.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadDialog();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentLoading();

        } else if (id == R.id.nav_history) {
            SearchHistory searchHistory = new SearchHistory();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, searchHistory)
                    .commit();

        } else if (id == R.id.nav_tools) {
            loadDialog();
        } else if (id == R.id.about_program) {
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, aboutFragment)
                    .commit();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            cityName = data.getExtras().getString(CITY_NAME);
            fragmentLoading();
        }
        if (resultCode == 100){
            recreate();

        }
    }
    private void fragmentLoading(){

        currentWeatherFragment = CurrentWeatherFragment.create(cityName);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, currentWeatherFragment)
                .commit();
    }

    private OnDialogListener dialogListener = new OnDialogListener() {

        @Override
        public void onDialogC() {
            Metrics metrics = Metrics.getInstance();
            if (metrics.isFahrenheit()) {
                metrics.setFahrenheit(false);
                fragmentLoading();
            }
        }

        @Override
        public void onDialogF() {
            Metrics metrics = Metrics.getInstance();
            metrics.setFahrenheit(false);
            if (!metrics.isFahrenheit()){
                metrics.setFahrenheit(true);
                fragmentLoading();
            }
        }
    };

    private void loadDialog(){
        SettingsDialogFragment settingsDialogFragment =
                SettingsDialogFragment.newInstance();
        settingsDialogFragment.setOnDialogListener(dialogListener);
        settingsDialogFragment.show(getSupportFragmentManager(),
                "dialog_fragment");
    }
}
