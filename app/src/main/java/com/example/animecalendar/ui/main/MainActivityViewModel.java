package com.example.animecalendar.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.data.local.LocalRepositoryImpl;

public class MainActivityViewModel extends AndroidViewModel {

    private final Application application;
    private final LocalRepository repository;

    public MainActivityViewModel(@NonNull Application application, AppDatabase appDatabase) {
        super(application);
        this.application = application;
        this.repository = new LocalRepositoryImpl();
    }

}
