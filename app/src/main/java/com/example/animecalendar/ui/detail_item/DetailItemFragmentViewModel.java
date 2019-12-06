package com.example.animecalendar.ui.detail_item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpisodeSingleItem;
import com.example.animecalendar.ui.main.MainActivityViewModel;

public class DetailItemFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public DetailItemFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<MyAnimeCharacter> getAnimeCharacter(long id) {
        return viewModel.getLocalRepository().getAnimeCharacter(id);
    }

    LiveData<AnimeEpisodeSingleItem> getAnimeEpisode(long id) {
        return viewModel.getLocalRepository().getAnimeEpisode(id);
    }

}
