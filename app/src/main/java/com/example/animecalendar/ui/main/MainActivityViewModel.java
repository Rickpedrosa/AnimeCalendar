package com.example.animecalendar.ui.main;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.data.local.LocalRepositoryImpl;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_episode.Datum;
import com.example.animecalendar.data.remote.repos.AnimeRepository;
import com.example.animecalendar.data.remote.repos.AnimeRepositoryImpl;
import com.example.animecalendar.data.remote.services.AnimeService;
import com.example.animecalendar.providers.RXJavaProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.animecalendar.model.CalendarAnimeEpisodesConstants.DUMMY_COLLAPSE;
import static com.example.animecalendar.model.CalendarAnimeEpisodesConstants.EXPAND_TITLE;
import static com.example.animecalendar.ui.calendar.CalendarFragmentViewAdapter.ANIME_TYPE;
import static com.example.animecalendar.ui.calendar.CalendarFragmentViewAdapter.EPISODE_TYPE;

public class MainActivityViewModel extends AndroidViewModel {

    private final Application application;
    @SuppressWarnings("FieldCanBeLocal")
    private Disposable disposable;
    private final LocalRepository localRepository;
    private final AnimeRepository animeRepository;
    private MutableLiveData<Boolean> progressBarController = new MutableLiveData<>();

    MainActivityViewModel(@NonNull Application application, AppDatabase appDatabase) {
        super(application);
        this.application = application;
        this.localRepository = new LocalRepositoryImpl(appDatabase.myAnimesEpisodesDao(), appDatabase.myAnimesDao());
        this.animeRepository = new AnimeRepositoryImpl();
    }

    public AnimeRepository getAnimeRepository() {
        return animeRepository;
    }

    public void progressBarLoading() {
        progressBarController.postValue(true);
    }

    public void progressBarStop() {
        progressBarController.postValue(false);
    }

    public LocalRepository getLocalRepository() {
        return localRepository;
    }

    public void retrieveRetroEpisodes(int animeId) {
        disposable = getEpisodesFromResponse(String.valueOf(animeId), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(animeEpisode -> setupEpisodesForInsertion(animeId, animeEpisode),
                        throwable -> Toast.makeText(application, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show(),
                        () -> Toast.makeText(application, "All episodes fetched", Toast.LENGTH_LONG).show());
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
            if (animeEpisode.getData().get(i).getAttributes().getNumber() == 1) {
                episode.setViewType(ANIME_TYPE);
                episode.setCollapse(EXPAND_TITLE);
            } else {
                episode.setViewType(EPISODE_TYPE);
                episode.setCollapse(DUMMY_COLLAPSE);
            }
            listEpisodes.add(episode);
        }
        addEpisodesToDatabase(listEpisodes);
    }

    private void addEpisodesToDatabase(List<MyAnimeEpisode> list) {
        localRepository.addEpisodes(list);
    }

    private Observable<AnimeEpisode> getEpisodesFromResponse(String id, int offset) {
        return RXJavaProvider.episodeObservableFlatMapped(animeRepository.getAnimeEpisodes(
                id,
                offset,
                AnimeService.LIMIT))
                .concatMap(animeEpisode -> {
                    if (animeEpisode.getLinks().getNext() == null) {
                        return RXJavaProvider.episodeObservableFlatMapped(
                                animeRepository.getAnimeEpisodes(
                                        id,
                                        offset,
                                        AnimeService.LIMIT));
                    } else {
                        return Observable.zip(RXJavaProvider.episodeObservableFlatMapped(
                                animeRepository.getAnimeEpisodes(
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
}
