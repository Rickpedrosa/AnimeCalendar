package com.example.animecalendar.ui.main;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.providers.RXJavaProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                thumb = "X";
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

    public void assignDateToEpisodes(List<Calendar> days, List<MyAnimeEpisodeListWithAnimeTitle> caps) {
        float daysCount = (float) days.size();
        float capsCount = (float) caps.size();
        float capsPerDay = (capsCount / daysCount);

        float restAux = 0;
        int capReference = -1;
        float capsPerDayAux;
        float totalCapsAux = capsCount;
        List<AnimeEpisodeDateUpdatePOJO> episodesToUpdate = new ArrayList<>();

        if (capsCount >= daysCount) {
            //READY TO UPDATE!
            for (int i = 0; i < daysCount; i++) {
                if (i == 0) { //día 1
                    for (int firstDay = 0; firstDay < (int) capsPerDay; firstDay++) { //capítulos del primer día
                        episodesToUpdate.add(new AnimeEpisodeDateUpdatePOJO(
                                caps.get(firstDay).getId(),
                                CustomTimeUtils.getDateFormatted(days.get(i).getTime())
                        ));
                        capReference = firstDay; // variable para ir pasando el indice del capitulo a actualizar
                    }
                    restAux = capsPerDay - ((int) capsPerDay); //parte decimal que pasa al día siguiente
                    totalCapsAux = totalCapsAux - (int) capsPerDay; //se resta X parte entera capítulo del total
                } else if (i != (daysCount - 1)) { //días entre el primero y el último
                    capsPerDayAux = capsPerDay + restAux;
                    for (int betweenDay = 0; betweenDay < (int) capsPerDayAux; betweenDay++) { //capítulos del día (parte entera de capsPerDayAux)
                        capReference++;
                        episodesToUpdate.add(new AnimeEpisodeDateUpdatePOJO(
                                caps.get(capReference).getId(),
                                CustomTimeUtils.getDateFormatted(days.get(i).getTime())
                        ));
                    }
                    restAux = capsPerDayAux - ((int) capsPerDayAux); //parte decimal que pasa al día siguiente
                    totalCapsAux = totalCapsAux - ((int) capsPerDayAux); //se resta la parte entera del total de caps
                } else {
                    for (int lastDay = 0; lastDay < (int) totalCapsAux; lastDay++) {
                        //el último día no se hace operación, se le asigna los
                        //capítulos que quedan y ya está
                        capReference++;
                        episodesToUpdate.add(new AnimeEpisodeDateUpdatePOJO(
                                caps.get(capReference).getId(),
                                CustomTimeUtils.getDateFormatted(days.get(i).getTime())
                        ));
                    }
                }
            }
        }
        localRepository.updateEpisodeDateToWatchPojoVersion(episodesToUpdate);
    }

}
