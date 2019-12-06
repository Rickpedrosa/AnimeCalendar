package com.example.animecalendar.data.local;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.animecalendar.data.local.daos.MyAnimeCharactersDao;
import com.example.animecalendar.data.local.daos.MyAnimesDao;
import com.example.animecalendar.data.local.daos.MyAnimesEpisodesDao;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.model.AnimeEpisodeSingleItem;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.model.MyAnimeEpisodesDailyList;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

public class LocalRepositoryImpl implements LocalRepository {

    private final MyAnimesEpisodesDao myAnimesEpisodesDao;
    private final MyAnimesDao myAnimesDao;
    private final MyAnimeCharactersDao myAnimeCharactersDao;

    @Override
    public LiveData<AnimeEpisodeSingleItem> getAnimeEpisode(long id) {
        return myAnimesEpisodesDao.getAnimeEpisode(id);
    }

    @Override
    public LiveData<MyAnimeCharacter> getAnimeCharacter(long characterId) {
        return myAnimeCharactersDao.getAnimeCharacter(characterId);
    }

    public LocalRepositoryImpl(MyAnimesEpisodesDao myAnimesEpisodesDao,
                               MyAnimesDao myAnimesDao,
                               MyAnimeCharactersDao myAnimeCharactersDao) {
        this.myAnimesEpisodesDao = myAnimesEpisodesDao;
        this.myAnimesDao = myAnimesDao;
        this.myAnimeCharactersDao = myAnimeCharactersDao;
    }

    @Override
    public LiveData<List<AnimesForSeries>> getAnimesToExpose() {
        return myAnimesDao.getAnimesToExpose();
    }

    @Override
    public LiveData<MyAnime> getAnimeForDetail(int id) {
        return myAnimesDao.getAnimeForDetail(id);
    }

    @Override
    public void addAnime(MyAnime myAnime) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesDao.addAnime(myAnime));
    }

    @Override
    public void deleteAnime(int myAnime) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesDao.deleteAnime(myAnime));
    }

    @Override
    public LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id) {
        return myAnimesEpisodesDao.getAnimeEpisodes(id);
    }

    @Override
    public void addEpisodes(List<MyAnimeEpisode> episodes) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.addEpisodes(episodes));
    }

    @Override
    public void updateAnimeStatus(String status, int animeId) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesDao.updateAnimeStatus(status, animeId));
    }

    @Override
    public LiveData<List<CalendarAnime>> getAnimesToExposeForCalendar() {
        return myAnimesDao.getAnimesToExposeForCalendar();
    }

    @Override
    public LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getAnimeEpisodesToAssignDate(int animeId) {
        return myAnimesEpisodesDao.getAnimeEpisodesToAssignDate(animeId);
    }

    @Override
    public void updateEpisodeDateToWatchPojoVersion(List<AnimeEpisodeDateUpdatePOJO> episodes) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.updateEpisodeDateToWatchPojoVersion(episodes));
    }

    @Override
    public LiveData<List<AnimesForSeries>> getAnimesToExposeByCategory(String category) {
        return myAnimesDao.getAnimesToExposeByCategory(category);
    }

    @Override
    public void updateEpisodeStatusAndDatePOJO(AnimeEpDateStatusPOJO episode) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.updateEpisodeStatusAndDatePOJO(episode));
    }

    @Override
    public LiveData<List<String>> getTodayItems(String today) {
        return myAnimesDao.getTodayItems(today);
    }

    @Override
    public void addEpisodesWithReplace(List<MyAnimeEpisode> episodes) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.addEpisodesWithReplace(episodes));
    }

    @Override
    public LiveData<List<MyAnimeCharacter>> getAnimeCharacters(long animeId) {
        return myAnimeCharactersDao.getAnimeCharacters(animeId);
    }

    @Override
    public void addAnimeCharacters(List<MyAnimeCharacter> characters) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimeCharactersDao.addAnimeCharacters(characters));
    }

    @Override
    public void deleteAnimeCharacters(long animeId) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimeCharactersDao.deleteAnimeCharacters(animeId));
    }

    @Override
    public LiveData<List<MyAnimeCharacter>> getAnimeCharactersByQuery(long animeId, String query) {
        return myAnimeCharactersDao.getAnimeCharactersByQuery(animeId, query);
    }

    @Override
    public LiveData<Integer> checkIfAnimeHasCharacters(long animeId) {
        return myAnimeCharactersDao.checkIfAnimeHasCharacters(animeId);
    }

    @Override
    public void updateEpisodeStatus(int value, int episodeId) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.updateEpisodeStatus(
                value, episodeId)
        );
    }

    @Override
    public LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodesWithQuery(int id, String query) {
        return myAnimesEpisodesDao.getAnimeEpisodesWithQuery(id, query);
    }

    @Override
    public LiveData<List<MyAnimeEpisodesDailyList>> getEpisodesOfTheDay(String day) {
        return myAnimesEpisodesDao.getEpisodesOfTheDay(day);
    }

    @Override
    public LiveData<List<AnimesForSeries>> getAnimesToExposeBySearchQuery(String query) {
        return myAnimesDao.getAnimesToExposeBySearchQuery(query);
    }

    @Override
    public LiveData<List<AnimeEpisodeDates>> getDatesFromWatchableEpisodes() {
        return myAnimesEpisodesDao.getDatesFromWatchableEpisodes();
    }

}
