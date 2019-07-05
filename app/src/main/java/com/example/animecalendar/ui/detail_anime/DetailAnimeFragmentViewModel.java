package com.example.animecalendar.ui.detail_anime;

import android.util.Log;

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
                .subscribe(animeEpisode -> setupEpisodesForInsertion(animeId, animeEpisode),
                        throwable -> progressTrigger.postValue(false),
                        () -> progressTrigger.postValue(false));
    }

    private void setupEpisodesForInsertion(long animeId, AnimeEpisode animeEpisode) {
        List<MyAnimeEpisode> listEpisodes = new ArrayList<>();
        MyAnimeEpisode episode;

        String thumb, title, synopsis, airDate;
        int seasonNumber, number, length;
        for (int i = 0; i < animeEpisode.getData().size(); i++) {
            if (animeEpisode.getData().get(i).getAttributes().getThumbnail() == null) {
                thumb = "";
            } else {
                thumb = animeEpisode.getData().get(i).getAttributes().getThumbnail().getOriginal();
            }
            if (animeEpisode.getData().get(i).getAttributes().getCanonicalTitle() == null) {
                title = "Title not available";
            } else {
                title = animeEpisode.getData().get(i).getAttributes().getCanonicalTitle();
            }
            if (animeEpisode.getData().get(i).getAttributes().getSeasonNumber() == null) {
                seasonNumber = -1;
            } else {
                seasonNumber = animeEpisode.getData().get(i).getAttributes().getSeasonNumber();
            }
            if (animeEpisode.getData().get(i).getAttributes().getNumber() == null) {
                number = -1;
            } else {
                number = animeEpisode.getData().get(i).getAttributes().getNumber();
            }
            if (animeEpisode.getData().get(i).getAttributes().getSynopsis() == null) {
                synopsis = "Synopsis not available";
            } else {
                synopsis = animeEpisode.getData().get(i).getAttributes().getSynopsis();
            }
            if (animeEpisode.getData().get(i).getAttributes().getAirdate() == null) {
                airDate = "Air date not available";
            } else {
                airDate = animeEpisode.getData().get(i).getAttributes().getAirdate();
            }
            if (animeEpisode.getData().get(i).getAttributes().getLength() == null) {
                length = 0;
            } else {
                length = animeEpisode.getData().get(i).getAttributes().getLength();
            }
            episode = new MyAnimeEpisode(
                    Long.parseLong(animeEpisode.getData().get(i).getId()),
                    animeId,
                    title,
                    seasonNumber,
                    number,
                    synopsis,
                    airDate,
                    length,
                    thumb,
                    0,
                    "-"
            );
            listEpisodes.add(episode);
        }
        addEpisodesToDatabase(listEpisodes);
    }

    private void addEpisodesToDatabase(List<MyAnimeEpisode> list) {
        viewModel.getLocalRepository().addEpisodes(list);
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

    LiveData<Boolean> getProgressTrigger() {
        return progressTrigger;
    }
}
