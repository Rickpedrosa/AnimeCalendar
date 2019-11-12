package com.example.animecalendar.ui.main;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.animecalendar.R;
import com.example.animecalendar.base.Event;
import com.example.animecalendar.base.pref.SharedPreferencesBooleanLiveData;
import com.example.animecalendar.base.pref.SharedPreferencesIntegerLiveData;
import com.example.animecalendar.base.pref.SharedPreferencesStringLiveData;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.data.local.LocalRepositoryImpl;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.AnimeCharacterIDs;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.DatumCharacter;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.anime_episode.Datum;
import com.example.animecalendar.data.remote.repos.AnimeRepository;
import com.example.animecalendar.data.remote.repos.AnimeRepositoryImpl;
import com.example.animecalendar.data.remote.services.AnimeService;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.model.NotificationItem;
import com.example.animecalendar.providers.RXJavaProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends AndroidViewModel {

    private final Application application;
    @SuppressWarnings("FieldCanBeLocal")
    private Disposable disposable;
    private final LocalRepository localRepository;
    private final AnimeRepository animeRepository;
    private MutableLiveData<Boolean> progressBarController = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> updateTrigger = new MutableLiveData<>();
    private MediatorLiveData<NotificationItem> todaysWatching = new MediatorLiveData<>();
    private final LiveData<Boolean> confirmationDialogPreference;
    private final LiveData<Boolean> notificationEnablingPreference;
    private final LiveData<String> defaultListTypePreference;
    private final LiveData<Integer> timeNotificationPreference;

    MainActivityViewModel(@NonNull Application application, AppDatabase appDatabase) {
        super(application);
        this.application = application;
        this.localRepository = new LocalRepositoryImpl(appDatabase.myAnimesEpisodesDao(), appDatabase.myAnimesDao());
        this.animeRepository = new AnimeRepositoryImpl();
        this.confirmationDialogPreference = new SharedPreferencesBooleanLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.reorder_key),
                true
        );
        this.defaultListTypePreference = new SharedPreferencesStringLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.anime_list_key),
                application.getResources().getString(R.string.anime_list_defaultValue)
        );
        this.notificationEnablingPreference = new SharedPreferencesBooleanLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.notification_key),
                true
        );
        this.timeNotificationPreference = new SharedPreferencesIntegerLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.time_notification_key),
                application.getResources().getInteger(R.integer.default_value_time_notif)
        );

        setupNotificationData();
    }

    private void setupNotificationData() {
        final LiveData<List<String>> liveDataTitles = localRepository.getTodayItems(CustomTimeUtils.getDateFormatted(new Date()));
        final LiveData<Integer> time = timeNotificationPreference;
        final LiveData<Boolean> triggerNotification = notificationEnablingPreference;

        todaysWatching.addSource(triggerNotification, aBoolean -> todaysWatching.setValue(combine(liveDataTitles, time, triggerNotification)));
        todaysWatching.addSource(liveDataTitles, strings -> todaysWatching.setValue(combine(liveDataTitles, time, triggerNotification)));
        todaysWatching.addSource(time, integer -> todaysWatching.setValue(combine(liveDataTitles, time, triggerNotification)));
    }

    LiveData<NotificationItem> getNotificationLiveData() {
        return todaysWatching;
    }

    private NotificationItem combine(LiveData<List<String>> liveDataTitles,
                                     LiveData<Integer> liveDataTime,
                                     LiveData<Boolean> input) {
        List<String> mTitles = new ArrayList<>();
        int mTime = 0;
        boolean access = false;
        if (input.getValue() != null && liveDataTime.getValue() != null && liveDataTitles.getValue() != null) {
            mTime = liveDataTime.getValue();
            mTitles = liveDataTitles.getValue();
            access = input.getValue();
        }
        return new NotificationItem(mTitles, mTime, access);
    }

    public LiveData<Boolean> getConfirmationDialogPreference() {
        return confirmationDialogPreference;
    }

    public LiveData<String> getDefaultListTypePreference() {
        return defaultListTypePreference;
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

    public void retrieveRetroEpisodes(int animeId, boolean replace) {
        disposable = getEpisodesFromResponse(String.valueOf(animeId), 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> progressBarLoading())
                .subscribe(animeEpisode -> setupEpisodesForInsertion(animeId, animeEpisode, replace),
                        throwable -> asyncStateInform(throwable.getLocalizedMessage()),
                        () -> asyncStateInform(application.getResources().getString(R.string.ep_fetching_completed)));
    }

    private void setupEpisodesForInsertion(long animeId, AnimeEpisode animeEpisode, boolean replace) {
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
                title = application.getResources().getString(R.string.episode_insert_title_not);
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
                synopsis = application.getResources().getString(R.string.episode_insert_synopsis_not);
            } else {
                synopsis = animeEpisode.getData().get(i).getAttributes().getSynopsis();
            }
            if (animeEpisode.getData().get(i).getAttributes().getAirdate() == null) {
                airDate = "Air date not available"; //UNUSED
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
                    LocalRepository.NOT_WATCHED,
                    LocalRepository.WATCH_DATE_NULL
            );
            listEpisodes.add(episode);
        }
        if (replace) {
            localRepository.addEpisodesWithReplace(listEpisodes);
        } else {
            addEpisodesToDatabase(listEpisodes);
        }
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

    public void checkIfCanBeUpdated(AnimesForSeries anime) {
        disposable = animeRepository.getAnime(String.valueOf(anime.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> progressBarLoading())
                .subscribe(animeResponse -> {
                    if (!Objects.requireNonNull(animeResponse.body()).getData().getAttributes().getStatus().equalsIgnoreCase(anime.getStatus())) {
                        updateTrigger.postValue(new Event<>(false));
                    } else {
                        updateTrigger.postValue(new Event<>(true));
                    }
                    progressBarStop();
                }, throwable -> asyncStateInform(throwable.getLocalizedMessage()));
    }

    public LiveData<Event<Boolean>> getUpdateTrigger() {
        return updateTrigger;
    }

    public LiveData<Boolean> getProgressBarController() {
        return progressBarController;
    }

    private void asyncStateInform(String message) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show();
        progressBarStop();
    }

    public void reorderCaps(List<AnimeEpisodeDateUpdatePOJO> nonWatchedEps) throws ParseException {
        AnimeEpisodeDateUpdatePOJO targetEpisode = nonWatchedEps.get(0); //Episode to be updated
        localRepository.updateEpisodeStatusAndDatePOJO(new AnimeEpDateStatusPOJO(
                targetEpisode.getId(),
                LocalRepository.WATCH_DATE_DONE,
                LocalRepository.WATCHED
        ));
        nonWatchedEps.remove(0);

        boolean updateListFlag = false;

        for (int i = 0; i < nonWatchedEps.size(); i++) {
            if (nonWatchedEps.size() == 1) {
                updateListFlag = true;
            } else {
                if (i == 0) {
                    if (targetEpisode.getNewDate().equals(nonWatchedEps.get(i).getNewDate())) {
                        //si es igual no hacer nada, si es distinto restar un día al resto de caps y actualizar
                        break;
                    } else {
                        updateListFlag = true;
                    }
                }
            }
            String day = nonWatchedEps.get(i).getNewDate();
            long mDay = CustomTimeUtils.dateFromStringToLong(day) - CustomTimeUtils.ONE_DAY_MILLISECONDS;
            String newDay = CustomTimeUtils.getDateFormatted(new Date(mDay));
            nonWatchedEps.get(i).setNewDate(newDay);
        }

        if (updateListFlag) {
            localRepository.updateEpisodeDateToWatchPojoVersion(nonWatchedEps);
        }
    }
}
