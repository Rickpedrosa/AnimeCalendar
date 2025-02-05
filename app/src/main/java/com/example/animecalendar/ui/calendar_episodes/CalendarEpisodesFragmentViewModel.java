package com.example.animecalendar.ui.calendar_episodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.text.ParseException;
import java.util.List;

public class CalendarEpisodesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int episodeToReorderPosition;

    public CalendarEpisodesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<MyAnimeEpisodesList>> getEpisodes(long animeId) {
        return viewModel.getLocalRepository().getAnimeEpisodes(animeId);
    }

    void updateAnimeStatus(long animeId) {
        viewModel.getLocalRepository().updateAnimeStatus(LocalRepository.STATUS_COMPLETED, animeId);
    }

    void reorderCaps(List<AnimeEpisodeDateUpdatePOJO> nonWatchedEps) throws ParseException {
        viewModel.reorderCaps(nonWatchedEps);
    }

    void updateStatusAndDateEpisode(AnimeEpDateStatusPOJO episode) {
        viewModel.getLocalRepository().updateEpisodeStatusAndDatePOJO(episode);
    }

    LiveData<Boolean> reorderCapsConfirmationPreference() {
        return viewModel.getConfirmationDialogPreference();
    }

    void updateEpisodeStatusOnly(int watched, int id) {
        viewModel.getLocalRepository().updateEpisodeStatus(
                watched,
                id
        );
    }

    int getEpisodeToReorderPosition() {
        return episodeToReorderPosition;
    }

    void setEpisodeToReorderPosition(int episodeToReorderPosition) {
        this.episodeToReorderPosition = episodeToReorderPosition;
    }
}
