package com.example.animecalendar.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.animecalendar.data.local.LocalRepository;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final LocalRepository repository;

    public MainActivityViewModelFactory(Application application, LocalRepository repository) {
        this.application = application;
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(application, repository);
    }
}
