package com.example.animecalendar.ui.calendar;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public CalendarFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<CalendarAnimeEpisodes>> getAnimeWithEpisodes() {
        return viewModel.getLocalRepository().getAnimeEpisodesForCalendar();
    }

    void updateEpisodeStatus(int value, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeStatus(value, episodeId);
    }

    void updateEpisodeViewType(int viewType, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeViewType(viewType, episodeId);
    }

    void updateEpisodeCollapse(int collapse, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeCollapse(collapse, episodeId);
    }

    void assignDateToEpisodes(List<Calendar> days, List<CalendarAnimeEpisodes> caps) {
       viewModel.assignDateToEpisodes(days, caps);
    }


}
