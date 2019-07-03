package com.example.animecalendar.providers;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.animecalendar.R;

public class AppbarConfigProvider {

    public static AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(
                R.id.myAnimeSeriesFragment,
                R.id.calendarFragment,
                R.id.searchFragment)
                .build();
    }

    private AppbarConfigProvider() {

    }
}
