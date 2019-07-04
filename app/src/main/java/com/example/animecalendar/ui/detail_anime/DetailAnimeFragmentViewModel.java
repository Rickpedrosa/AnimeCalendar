package com.example.animecalendar.ui.detail_anime;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_episode.Datum;
import com.example.animecalendar.data.remote.services.AnimeService;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.providers.RXJavaProvider;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailAnimeFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private Disposable disposable;
    private int counter = 0;
    private MutableLiveData<Boolean> progressTrigger = new MutableLiveData<>();

    public DetailAnimeFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<MyAnime> getAnime(int id) {
        return viewModel.getLocalRepository().getAnimeForDetail(id);
    }

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id) {
        return viewModel.getLocalRepository().getAnimeEpisodes(id);
    }

    private void addEpisodes(List<MyAnimeEpisode> list) {
        viewModel.getLocalRepository().addEpisodes(list);
    }

    void disposeObservable() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    void retrieveRetroEpisodes(int animeId) {
        disposable = getEpisodesFromResponse(String.valueOf(animeId), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(animeEpisode -> {
                    List<MyAnimeEpisode> listEpisodes = new ArrayList<>();
                    MyAnimeEpisode episode;
                    String thumb;
                    for (int i = 0; i < animeEpisode.getData().size(); i++) {
                        if (animeEpisode.getData().get(i).getAttributes().getThumbnail() == null) {
                            thumb = "";
                        } else {
                            thumb = animeEpisode.getData().get(i).getAttributes().getThumbnail().getOriginal();
                        }
                        episode = new MyAnimeEpisode(
                                Long.parseLong(animeEpisode.getData().get(i).getId()),
                                (long) animeId,
                                animeEpisode.getData().get(i).getAttributes().getCanonicalTitle(),
                                animeEpisode.getData().get(i).getAttributes().getSeasonNumber(),
                                animeEpisode.getData().get(i).getAttributes().getNumber(),
                                animeEpisode.getData().get(i).getAttributes().getSynopsis(),
                                animeEpisode.getData().get(i).getAttributes().getAirdate(),
                                animeEpisode.getData().get(i).getAttributes().getLength(),
                                thumb,
                                0,
                                "-"
                        );
                        listEpisodes.add(episode);
                    }
                    addEpisodes(listEpisodes);
                }, throwable -> {
                    progressTrigger.postValue(false);
                    Log.d("PETADA", throwable.getMessage());
                }, () -> progressTrigger.postValue(false));
    }

    private Observable<AnimeEpisode> getEpisodesFromResponse(String id, int offset) {
        return RXJavaProvider.episodeObservableFlatMapped(viewModel.getAnimeRepository().getAnimeEpisodes(
                id,
                offset,
                AnimeService.LIMIT))
                .concatMap(animeEpisode -> {
                    // updateCallProgress(animeEpisode, total);
                    progressTrigger.postValue(true);
                    if (animeEpisode.getLinks().getNext() == null) {
                        return RXJavaProvider.episodeObservableFlatMapped(
                                viewModel.getAnimeRepository().getAnimeEpisodes(
                                        id,
                                        offset,
                                        AnimeService.LIMIT));
                    } else {
                        return Observable.zip(RXJavaProvider.episodeObservableFlatMapped(
                                viewModel.getAnimeRepository().getAnimeEpisodes(
                                        id,
                                        offset,
                                        AnimeService.LIMIT)),
                                getEpisodesFromResponse(
                                        id,
                                        (offset + AnimeService.PLUS_OFFSET)),
                                (epOne, epTwo) -> {
                                    AnimeEpisode theTrueOne = new AnimeEpisode();
                                    List<Datum> datum = new ArrayList<>(epOne.getData());
                                    datum.addAll(epTwo.getData());
                                    theTrueOne.setMeta(epTwo.getMeta());
                                    theTrueOne.setLinks(epTwo.getLinks());
                                    theTrueOne.setData(datum);
                                    return theTrueOne;
                                });
                    }
                });
    }

    public LiveData<Boolean> getProgressTrigger() {
        return progressTrigger;
    }
}
