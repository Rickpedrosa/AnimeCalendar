package com.example.animecalendar.ui.characters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.base.Resource;
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

public class CharactersFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public CharactersFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<Boolean> getCharactersLoading() {
        return viewModel.getProgressBarCharacterController();
    }

    void retrieveCharacters(int id) {
        viewModel.testAnimeCharacterIDSApiCall(id);
    }

    LiveData<Resource<String>> getCharacterAsyncInfo(){
        return viewModel.getResourceCharacter();
    }

    LiveData<List<MyAnimeCharacter>> getAnimeCharacters(long animeId){
        return viewModel.getLocalRepository().getAnimeCharacters(animeId);
    }
}
