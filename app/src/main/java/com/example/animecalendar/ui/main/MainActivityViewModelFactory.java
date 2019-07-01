package com.example.animecalendar.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.animecalendar.data.local.AppDatabase;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final AppDatabase appDatabase;

    public MainActivityViewModelFactory(Application application, AppDatabase appDatabase) {
        this.application = application;
        this.appDatabase = appDatabase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(application, appDatabase);
    }
}
