package com.example.animecalendar.ui.series;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public MyAnimeSeriesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
