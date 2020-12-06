package com.example.weatherforecast;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.weatherforecast.modelCurrentWeather.OnDialogListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final static String CITY_NAME = "CityName";
    public static String cityName;
    private WirelessConnectionLost wirelessConnectionLost;
    private SharedPreferences sharedPref;
    private float lng;
    private float lat;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private NavigationView navigationView;
    private View header;

    // Используется, чтобы определить результат Activity регистрации через
    // Google
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    // Клиент для регистрации пользователя через Google
    private GoogleSignInClient googleSignInClient;

    // Кнопка регистрации через Google
    private com.google.android.gms.common.SignInButton buttonSignIn;

    // Кнопка выхода из Гугл
    private MaterialButton buttonSingOut;

    CurrentWeatherFragment currentWeatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getPreferences(MODE_PRIVATE);
        cityName = sharedPref.getString(CITY_NAME, null);
        requestPemissions();
        wirelessConnectionLost = new WirelessConnectionLost();
        registerReceiver(wirelessConnectionLost, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        initNotificationChannel();
        initNotificationChannel2();
        Metrics metrics = Metrics.getInstance();
        metrics.setFahrenheit(sharedPref.getBoolean("METRICS", false));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initGetToken();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        header = navigationView.getHeaderView(0);
        buttonSignIn = header.findViewById(R.id.sign_in_button);
        buttonSingOut = header.findViewById(R.id.sing_out_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                signIn();
                                            }
                                        }
        );

        buttonSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Проверим, входил ли пользователь в это приложение через Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            buttonSignIn.setEnabled(false);
            buttonSignIn.setVisibility(View.GONE);
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            updateUI(account.getDisplayName());
        }

    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("3", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void initNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("4", "push-messages", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wirelessConnectionLost);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
    protected void onPause() {
        super.onPause();
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
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(CITY_NAME, cityName).commit();
                fragmentLoading(cityName, 0, 0);
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
                fragmentLoading(cityName, lat, lng);
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
        MenuItem map = menu.findItem(R.id.action_map);
        map.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadMap();
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
            fragmentLoading(cityName, 0, 0);

        } else if (id == R.id.nav_history) {
            SearchHistory searchHistory = new SearchHistory();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, searchHistory)
                    .addToBackStack("HISTORY")
                    .commit();

        } else if (id == R.id.nav_tools) {
            loadDialog();
        } else if (id == R.id.about_program) {
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, aboutFragment)
                    .addToBackStack("ABOUT_PROGRAM")
                    .commit();
        } else if (id == R.id.nav_map){
            loadMap();
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
            fragmentLoading(cityName, lat, lng);
        }
        if (resultCode == 100){
            recreate();

        }
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты по нему уже готовы.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Выход из учетной записи в приложении
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI("User Name");
                        enableSign();
                    }
                });
    }

    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получение данных пользователя
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Регистрация прошла успешно
            disableSign();
            updateUI(account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    // Обновить данные о пользователе на экране
    private void updateUI(String userName) {
        TextView displayName = header.findViewById(R.id.displayName);
       displayName.setText(userName);
    }

    private void enableSign(){
        buttonSignIn.setVisibility(View.VISIBLE);
        buttonSignIn.setEnabled(true);
        buttonSingOut.setEnabled(false);
    }

    private void disableSign(){
        buttonSignIn.setEnabled(false);
        buttonSignIn.setVisibility(View.GONE);
        buttonSingOut.setEnabled(true);
    }

    private void fragmentLoading(String cityName, float lat, float lng){

        currentWeatherFragment = CurrentWeatherFragment.create(cityName, lat, lng);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, currentWeatherFragment)
                .addToBackStack("WEATHER_FRAGMENT")
                .commit();
    }

    private OnDialogListener dialogListener = new OnDialogListener() {

        @Override
        public void onDialogC() {
            Metrics metrics = Metrics.getInstance();
            if (metrics.isFahrenheit()) {
                metrics.setFahrenheit(false);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("METRICS", metrics.isFahrenheit()).commit();
                cityName = sharedPref.getString(CITY_NAME, cityName);
                fragmentLoading(cityName, 0, 0);
            }
        }

        @Override
        public void onDialogF() {
            Metrics metrics = Metrics.getInstance();
            metrics.setFahrenheit(false);
            if (!metrics.isFahrenheit()){
                metrics.setFahrenheit(true);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("METRICS", metrics.isFahrenheit()).commit();
                cityName = sharedPref.getString(CITY_NAME, cityName);
                fragmentLoading(cityName,0,0);
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
    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("PushMessage", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("PUSH", token);
                    }
                });
    }
    private void requestPemissions() {
        // Проверим, есть ли Permission’ы, и если их нет, запрашиваем их у
        // пользователя
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем координаты
            requestLocation();
        } else {
            // Permission’ов нет, запрашиваем их у пользователя
            requestLocationPermissions();
        }
    }
    // Запрашиваем координаты
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            fragmentLoading(cityName, 0, 0);
            return;
        }

        // Получаем менеджер геолокаций
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 10 минут или каждые
            // 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10000, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = (float)location.getLatitude(); // Широта
                    lng = (float)location.getLongitude(); // Долгота
                    fragmentLoading(cityName, lat, lng);
                }
            });
        } else fragmentLoading(cityName, lat, lng);
        if (lat == 0 && lng ==0){
            provider = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(provider, 10000, 1000, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    lat = (float)location.getLatitude(); // Широта
                    lng = (float)location.getLongitude(); // Долгота
                    fragmentLoading(cityName, lat, lng);
                }
            });
        }
    }

    // Запрашиваем Permission’ы для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            // Запрашиваем эти два Permission’а у пользователя
            ActivityCompat.requestPermissions(this,
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
    private void loadMap(){
        MapsFragment mapsFragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, mapsFragment)
                .addToBackStack("MAPS_FRAGMENT")
                .commit();
    }

}
