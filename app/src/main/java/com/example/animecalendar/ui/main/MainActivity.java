package com.example.animecalendar.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.animecalendar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.myAnimeSeriesFragment,
                        R.id.calendarFragment,
                        R.id.searchFragment)
                        .build();

        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

//        NavigationUI.setupActionBarWithNavController(this,
//                Objects.requireNonNull(navHost).getNavController(),
//                appBarConfiguration);

        BottomNavigationView bnv = ActivityCompat.requireViewById(this, R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bnv,
                Objects.requireNonNull(navHost).getNavController());
    }
}

