package com.example.animecalendar.data.local;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.animecalendar.data.local.daos.MyAnimesDao;
import com.example.animecalendar.data.local.daos.MyAnimesEpisodesDao;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpisodePOJOUpdate;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.CalendarAnimeEpisodesDeprecated;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

public class LocalRepositoryImpl implements LocalRepository {

    private final MyAnimesEpisodesDao myAnimesEpisodesDao;
    private final MyAnimesDao myAnimesDao;

    public LocalRepositoryImpl(MyAnimesEpisodesDao myAnimesEpisodesDao, MyAnimesDao myAnimesDao) {
        this.myAnimesEpisodesDao = myAnimesEpisodesDao;
        this.myAnimesDao = myAnimesDao;
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
    public LiveData<List<CalendarAnimeEpisodesDeprecated>> getAnimeEpisodesForCalendar() {
        return myAnimesEpisodesDao.getAnimeEpisodesForCalendar();
    }

    @Override
    public void updateEpisodeStatus(int value, int id) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.updateEpisodeStatus(value, id));
    }

    @Override
    public void updateEpisodeDateToWatch(String date, int episodeId) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> myAnimesEpisodesDao.updateEpisodeDateToWatch(date, episodeId));
    }

    @Override
    public LiveData<List<CalendarAnime>> getAnimesToExposeForCalendar() {
        return myAnimesDao.getAnimesToExposeForCalendar();
    }

}
