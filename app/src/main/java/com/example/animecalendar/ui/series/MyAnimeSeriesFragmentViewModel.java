package com.example.animecalendar.ui.series;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int itemPosition;


    public MyAnimeSeriesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<AnimesForSeries>> getAnimesToExpose() {
        return viewModel.getLocalRepository().getAnimesToExpose();
    }

    int getItemPosition() {
        return itemPosition;
    }

    void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    void deleteAnime(int id) {
        viewModel.getLocalRepository().deleteAnime(id);
    }

    void updateStatus(@SuppressWarnings("SameParameterValue") String status, int id) {
        viewModel.getLocalRepository().updateAnimeStatus(status, id);
    }
}
