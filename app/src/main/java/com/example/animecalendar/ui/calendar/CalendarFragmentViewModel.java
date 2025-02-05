package com.example.animecalendar.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.Calendar;
import java.util.List;

public class CalendarFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int flag = 2;

    public CalendarFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<CalendarAnime>> getAnimeWithEpisodes() {
        return viewModel.getLocalRepository().getAnimesToExposeForCalendar();
    }

    void updateAnimeStatus(long animeId){
        viewModel.getLocalRepository().updateAnimeStatus(LocalRepository.STATUS_COMPLETED, animeId);
    }

//    LiveData<List<String>> getDatesFromWatchableEpisodes() {
//        return viewModel.getLocalRepository().getDatesFromWatchableEpisodes();
//    }

    int getFlag() {
        return flag;
    }

    void setFlag() {
        flag++;
    }
}
