package com.example.animecalendar.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.animecalendar.ui.main.MainActivityViewModel;

public class ViewModelFragmentFactory implements ViewModelProvider.Factory {

    private final MainActivityViewModel mainActivityViewModel;

    public ViewModelFragmentFactory(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ViewModelFragment(mainActivityViewModel);
    }
}
