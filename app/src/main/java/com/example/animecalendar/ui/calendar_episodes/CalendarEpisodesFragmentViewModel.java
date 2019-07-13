package com.example.animecalendar.ui.calendar_episodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class CalendarEpisodesFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public CalendarEpisodesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<MyAnimeEpisodesList>> getEpisodes(int animeId) {
        return viewModel.getLocalRepository().getAnimeEpisodes(animeId);
    }

    void updateEpisodeStatus(int value, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeStatus(value, episodeId);
    }

    void updateEpisodeDateToWatch(String date, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeDateToWatch(date, episodeId);
    }

}
