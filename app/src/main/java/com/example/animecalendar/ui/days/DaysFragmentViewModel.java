package com.example.animecalendar.ui.days;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class DaysFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public DaysFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
