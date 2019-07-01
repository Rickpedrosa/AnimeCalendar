package com.example.animecalendar.ui.calendar;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class CalendarFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public CalendarFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
