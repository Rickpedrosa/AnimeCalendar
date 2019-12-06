package com.example.animecalendar.ui.episodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class EpisodesFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private LiveData<List<MyAnimeEpisodesList>> myAnimeEpisodes;
    private boolean searchViewExpanded;

    public EpisodesFragmentViewModel(MainActivityViewModel mainActivityViewModel, int animeId) {
        this.viewModel = mainActivityViewModel;
        this.searchQuery.setValue("");
        this.myAnimeEpisodes = Transformations.switchMap(searchQuery,
                input -> {
                    if (input.equals("")) {
                        return viewModel.getLocalRepository().getAnimeEpisodes(animeId);
                    }
                    return viewModel.getLocalRepository().getAnimeEpisodesWithQuery(
                            animeId,
                            "%" + input + "%"
                    );
                });
    }

    public LiveData<List<MyAnimeEpisodesList>> getMyAnimeEpisodes() {
        return myAnimeEpisodes;
    }

    void setSearchQuery(String input) {
        searchQuery.setValue(input);
    }

    String getSearchQuery() {
        return searchQuery.getValue();
    }

    boolean isSearchViewExpanded() {
        return searchViewExpanded;
    }

    void setSearchViewExpanded(boolean searchViewExpanded) {
        this.searchViewExpanded = searchViewExpanded;
    }
}
