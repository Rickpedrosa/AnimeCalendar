package com.example.animecalendar.ui.series;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.MyAnimeList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public MyAnimeSeriesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public LiveData<List<AnimesForSeries>> getAnimesToExpose() {
        return viewModel.getLocalRepository().getAnimesToExpose();
    }
}
