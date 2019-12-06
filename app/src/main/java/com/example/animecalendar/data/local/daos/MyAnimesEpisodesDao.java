package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.model.AnimeEpisodeSingleItem;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.model.MyAnimeEpisodesDailyList;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface MyAnimesEpisodesDao {

    @Query("SELECT id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate  " +
            "FROM episodes WHERE animeId = :id ORDER BY number")
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    @Query("SELECT canonicalTitle, thumbnail, synopsis " +
            "FROM episodes WHERE id = :id")
    LiveData<AnimeEpisodeSingleItem> getAnimeEpisode(long id);

    @Query("SELECT id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate  " +
            "FROM episodes WHERE animeId = :id AND canonicalTitle LIKE :query ORDER BY number")
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodesWithQuery(int id, String query);

    @Query("SELECT ep.id, ep.animeId, ep.canonicalTitle, ep.number, ep.thumbnail, ep.wasWatched, an.canonicalTitle AS animeTitle " +
            "FROM episodes ep INNER JOIN anime an ON ep.animeId = an.id " +
            "WHERE ep.watchToDate LIKE :day AND ep.wasWatched = 0 AND an.status LIKE 'following'" +
            "ORDER BY an.id")
    LiveData<List<MyAnimeEpisodesDailyList>> getEpisodesOfTheDay(String day);

    @Query("SELECT ani.canonicalTitle AS animeTitle, ep.id, ep.animeId, ep.canonicalTitle, ep.number, ep.thumbnail, ep.wasWatched, ep.watchToDate " +
            "FROM episodes ep INNER JOIN anime ani ON ep.animeId = ani.id " +
            "WHERE (ep.watchToDate NOT LIKE '-' OR ep.watchToDate NOT LIKE 'Watched') AND ani.status LIKE 'following' AND ep.wasWatched = 0 " +
            "GROUP BY ani.id, ep.number " +
            "ORDER BY ep.watchToDate")
    LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getAnimeEpisodesForCalendarEvents();

    @Query("SELECT DISTINCT ep.watchToDate, COUNT(DISTINCT ep.id) AS epsCount" +
            " FROM episodes ep INNER JOIN anime an ON an.id = ep.animeId " +
            "WHERE ep.watchToDate LIKE '%/%' AND an.status LIKE 'following' " +
            "GROUP BY ep.watchToDate " +
            "ORDER BY date(ep.watchToDate)")
    LiveData<List<AnimeEpisodeDates>> getDatesFromWatchableEpisodes();

    @Query("SELECT ani.canonicalTitle AS animeTitle, ep.id, ep.animeId, ep.canonicalTitle, ep.number, ep.thumbnail, ep.wasWatched, ep.watchToDate " +
            "FROM episodes ep INNER JOIN anime ani ON ep.animeId = ani.id " +
            "WHERE ep.animeId = :animeId AND ep.wasWatched = 0 " +
            "ORDER BY number")
    LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getAnimeEpisodesToAssignDate(int animeId);

    @Query("SELECT ani.canonicalTitle AS animeTitle, ep.id, ep.animeId, ep.canonicalTitle, ep.number, ep.thumbnail, ep.wasWatched, ep.watchToDate " +
            "FROM episodes ep INNER JOIN anime ani ON ep.animeId = ani.id " +
            "WHERE ep.watchToDate = :date " +
            "ORDER BY ani.id")
    LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getAnimeEpisodesForASingleDate(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addEpisodes(List<MyAnimeEpisode> episodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEpisodesWithReplace(List<MyAnimeEpisode> episodes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void addEpisodesOnReplaceStrategy(List<MyAnimeEpisode> episodes);

    @Update(entity = MyAnimeEpisode.class)
    void updateEpisodeStatusAndDatePOJO(AnimeEpDateStatusPOJO episode);

    @Query("UPDATE episodes SET wasWatched = :value WHERE id = :episodeId")
    void updateEpisodeStatus(int value, int episodeId);

    @Update(entity = MyAnimeEpisode.class)
    void updateEpisodeDateToWatchPojoVersion(List<AnimeEpisodeDateUpdatePOJO> episodes);

    @Query("UPDATE episodes SET watchToDate = :date WHERE id = :episodeId")
    void updateEpisodeDateToWatch(String date, int episodeId);
}
