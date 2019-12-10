package com.example.animecalendar.ui.characters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.base.Resource;
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class CharactersFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private LiveData<List<MyAnimeCharacter>> myAnimeCharacters;
    private boolean searchViewExpanded;

    public CharactersFragmentViewModel(MainActivityViewModel viewModel, long animeId) {
        this.viewModel = viewModel;
        this.searchQuery.setValue("");
        this.myAnimeCharacters = Transformations.switchMap(searchQuery,
                input -> {
                    if (input.equals("")) {
                        return viewModel.getLocalRepository().getAnimeCharacters(animeId);
                    }
                    return viewModel.getLocalRepository().getAnimeCharactersByQuery(
                            animeId,
                            "%" + input + "%"
                    );
                });
    }

    LiveData<Boolean> getCharactersLoading() {
        return viewModel.getProgressBarCharacterController();
    }

    void retrieveCharacters(long id) {
        viewModel.testAnimeCharacterIDSApiCall(id);
    }

    LiveData<Resource<String>> getCharacterAsyncInfo() {
        return viewModel.getResourceCharacter();
    }

    LiveData<List<MyAnimeCharacter>> getAnimeCharactersV2() {
        return myAnimeCharacters;
    }

    public MainActivityViewModel getViewModel() {
        return viewModel;
    }

    boolean isSearchViewExpanded() {
        return searchViewExpanded;
    }

    void setSearchViewExpanded(boolean searchViewExpanded) {
        this.searchViewExpanded = searchViewExpanded;
    }

    void setSearchQuery(String input) {
        searchQuery.setValue(input);
    }

    String getSearchQuery() {
        return searchQuery.getValue();
    }

    LiveData<Integer> getAnimeCharactersWard(long animeId) {
        return viewModel.getLocalRepository().checkIfAnimeHasCharacters(animeId);
    }
}
