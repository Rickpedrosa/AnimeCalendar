package com.example.animecalendar.ui.detail_anime;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class DetailAnimeFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public DetailAnimeFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
