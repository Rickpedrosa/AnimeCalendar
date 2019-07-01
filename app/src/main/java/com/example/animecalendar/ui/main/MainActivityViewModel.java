package com.example.animecalendar.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.animecalendar.data.local.LocalRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private final Application application;
    private final LocalRepository repository;

    public MainActivityViewModel(@NonNull Application application, LocalRepository repository) {
        super(application);
        this.application = application;
        this.repository = repository;
    }


}
