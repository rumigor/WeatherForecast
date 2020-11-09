package com.example.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends BaseActivity {
    private static final String NIGHT_THEME = "darkTheme";
    private static final String CELSIUS = "celsiusDegree";
    private SwitchCompat theme;
    private final String NIGHT_MODE = "Night_Mode";
    private final static int REQUEST_CODE = 0x1FAB;
    boolean nightMode;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getApplicationContext(), "onCreate#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onCreate");
        setContentView(R.layout.activity_settings);
        theme = findViewById(R.id.nightTheme);
        layout = findViewById(R.id.backgroundLayout);
//        nightMode = getIntent().getExtras().getBoolean(NIGHT_THEME);
//        if (nightMode){
//            theme.setChecked(true);
//            setTheme(R.style.AppDarkTheme);
//        }
        Button cancel = findViewById(R.id.resetButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button saveSet = findViewById(R.id.saveButton);
        saveSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                nightMode = theme.isChecked();
//                intent.putExtra(NIGHT_THEME, nightMode);
//                setResult(100, intent);
                finish();
            }
        });
        theme.setChecked(isDarkTheme());

        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkTheme(isChecked);
                recreate();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume#3", Toast.LENGTH_SHORT).show();
        Log.d("SettingsActivity", "onResume");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NIGHT_MODE, theme.isChecked());
    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        boolean status = savedInstanceState.getBoolean(NIGHT_MODE);
//        if (theme.isChecked()) {
//            setTheme(R.style.AppDarkTheme);
//        }
//        else {
//            setTheme(R.style.AppTheme);
//        }
//    }
}