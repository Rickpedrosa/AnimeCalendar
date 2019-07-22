package com.example.animecalendar.ui.days;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class DaysFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public DaysFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<AnimeEpisodeDates>> getDays(){
        return viewModel.getLocalRepository().getDatesFromWatchableEpisodes();
    }
}
