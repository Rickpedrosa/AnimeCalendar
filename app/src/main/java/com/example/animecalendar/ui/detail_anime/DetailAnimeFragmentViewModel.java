package com.example.animecalendar.ui.detail_anime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class DetailAnimeFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private boolean collapseSynopsis = false;

    public DetailAnimeFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<MyAnime> getAnime(long id) {
        return viewModel.getLocalRepository().getAnimeForDetail(id);
    }

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(long id) {
        return viewModel.getLocalRepository().getAnimeEpisodes(id);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isCollapseSynopsis() {
        return collapseSynopsis;
    }

    void setCollapseSynopsis(boolean collapseSynopsis) {
        this.collapseSynopsis = collapseSynopsis;
    }

}
