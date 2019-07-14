package com.example.animecalendar.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.Calendar;
import java.util.List;

public class CalendarFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public CalendarFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<CalendarAnime>> getAnimeWithEpisodes() {
        return viewModel.getLocalRepository().getAnimesToExposeForCalendar();
    }

//    void assignDateToEpisodes(List<Calendar> days, List<CalendarAnimeEpisodesDeprecated> caps) {
//        viewModel.assignDateToEpisodes(days, caps);
//    }
}
