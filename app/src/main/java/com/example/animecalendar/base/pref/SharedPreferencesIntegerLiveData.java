package com.example.animecalendar.base.pref;

import android.content.SharedPreferences;

public class SharedPreferencesIntegerLiveData extends SharedPreferencesLiveData<Integer> {
    public SharedPreferencesIntegerLiveData(SharedPreferences sharedPreferences, String key, Integer defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    protected Integer getValueFromPreferences(String key, Integer defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
