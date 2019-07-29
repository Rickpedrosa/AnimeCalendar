package com.example.animecalendar.ui.days_episodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.MyAnimeEpisodesDailyList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class DaysEpisodesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;

    public DaysEpisodesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<MyAnimeEpisodesDailyList>> getEpisodes(String date) {
        return viewModel.getLocalRepository().getEpisodesOfTheDay(date);
    }

    void updateStatusAndDateEpisode(AnimeEpDateStatusPOJO episode) {
        viewModel.getLocalRepository().updateEpisodeStatusAndDatePOJO(episode);
    }

}
