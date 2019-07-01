package com.example.animecalendar.base;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class ViewModelFragment extends ViewModel {

    private MainActivityViewModel mainActivityViewModel;

    public ViewModelFragment(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }
}
