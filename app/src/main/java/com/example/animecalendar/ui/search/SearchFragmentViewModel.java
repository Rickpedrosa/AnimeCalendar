package com.example.animecalendar.ui.search;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class SearchFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public SearchFragmentViewModel(MainActivityViewModel mainActivityViewModel) {
        this.viewModel = mainActivityViewModel;
    }
}
