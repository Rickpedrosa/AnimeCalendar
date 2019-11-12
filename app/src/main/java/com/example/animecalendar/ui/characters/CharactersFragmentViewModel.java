package com.example.animecalendar.ui.characters;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.AnimeCharacterIDs;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.DatumCharacter;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CharactersFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;
    private Disposable disposable;
    private MutableLiveData<List<AnimeCharacterDetail>> characters = new MutableLiveData<>();

    public CharactersFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    void testAnimeCharacterIDSApiCall(int id) {
        Single<List<AnimeCharacterDetail>> pog =
                viewModel.getAnimeRepository().getAnimeCharactersIds(String.valueOf(id))
                        .subscribeOn(Schedulers.io())
                        .flatMap((Function<AnimeCharacterIDs, ObservableSource<List<DatumCharacter>>>)
                                animeCharacterIDs -> Observable.just(animeCharacterIDs.getData()))
                        .flatMapIterable(items -> items)
                        .flatMap(it -> viewModel.getAnimeRepository().getAnimeCharacterDetails(it.getId()))
                        .subscribeOn(Schedulers.io())
                        .onErrorResumeNext(Observable.empty())// DA ERRORES 404 INUTIL ASI QUE HAY QUE PONER ESTO
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread());
        disposable = pog.subscribe(it -> {
            characters.postValue(it);
            for (int i = 0; i < it.size(); i++) {
                Log.d("RXOMEGALOL", it.get(i).getData().getAttributes().getCanonicalName());
            }
        }, throwable -> Log.d("RXOMEGALOLERROR", Objects.requireNonNull(throwable.getMessage())));
    }

    LiveData<List<AnimeCharacterDetail>> getCharacters() {
        return characters;
    }
}
