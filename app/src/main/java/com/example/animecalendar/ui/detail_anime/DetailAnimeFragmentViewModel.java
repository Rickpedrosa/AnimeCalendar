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
    private boolean collapseEpisodes = false;

    public DetailAnimeFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<MyAnime> getAnime(int id) {
        return viewModel.getLocalRepository().getAnimeForDetail(id);
    }

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id) {
        return viewModel.getLocalRepository().getAnimeEpisodes(id);
    }

    public boolean isCollapseSynopsis() {
        return collapseSynopsis;
    }

    public void setCollapseSynopsis(boolean collapseSynopsis) {
        this.collapseSynopsis = collapseSynopsis;
    }

    public boolean isCollapseEpisodes() {
        return collapseEpisodes;
    }

    public void setCollapseEpisodes(boolean collapseEpisodes) {
        this.collapseEpisodes = collapseEpisodes;
    }
}
