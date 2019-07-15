package com.example.animecalendar.ui.series;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int itemPosition;
    private DatePicker datePicker;

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

    void updateStatus(String status, int id) {
        viewModel.getLocalRepository().updateAnimeStatus(status, id);
    }

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id) {
        return viewModel.getLocalRepository().getAnimeEpisodes(id);
    }
}
