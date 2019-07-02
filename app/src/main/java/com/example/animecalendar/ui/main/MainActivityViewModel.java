package com.example.animecalendar.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.data.local.LocalRepositoryImpl;
import com.example.animecalendar.data.remote.repos.AnimeRepository;
import com.example.animecalendar.data.remote.repos.AnimeRepositoryImpl;

public class MainActivityViewModel extends AndroidViewModel {

    private final Application application;
    private final LocalRepository repository;
    private final AnimeRepository animeRepository;
    private MutableLiveData<Boolean> progressBarController = new MutableLiveData<>();

    MainActivityViewModel(@NonNull Application application, AppDatabase appDatabase) {
        super(application);
        this.application = application;
        this.repository = new LocalRepositoryImpl();
        this.animeRepository = new AnimeRepositoryImpl();
    }

    public AnimeRepository getAnimeRepository() {
        return animeRepository;
    }

    public LiveData<Boolean> getProgressBarController() {
        return progressBarController;
    }

    public void progressBarLoading(){
        progressBarController.postValue(true);
    }

    public void progressBarStop(){
        progressBarController.postValue(false);
    }
}
