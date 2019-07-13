package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpisodePOJOUpdate;
import com.example.animecalendar.model.CalendarAnimeEpisodesDeprecated;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

@Dao
public interface MyAnimesEpisodesDao {

    @Query("SELECT id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate  " +
            "FROM episodes WHERE animeId = :id ORDER BY number")
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    @Query("SELECT ani.id AS animeId, ani.canonicalTitle AS animeTitle, ep.id AS id, " +
            "ep.canonicalTitle AS canonicalTitle, ep.length, ep.number, ep.watchToDate, ep.wasWatched, " +
            "ep.viewType AS viewType, ep.collapse AS collapse " +
            "FROM episodes ep INNER JOIN anime ani ON ep.animeId = ani.id " +
            "WHERE ani.status LIKE 'following'" +
            "GROUP BY ani.id, ep.number " +
            "ORDER BY ani.id, ep.number")
    LiveData<List<CalendarAnimeEpisodesDeprecated>> getAnimeEpisodesForCalendar();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addEpisodes(List<MyAnimeEpisode> episodes);

    @Query("UPDATE episodes SET wasWatched = :value WHERE id = :episodeId")
    void updateEpisodeStatus(int value, int episodeId);

    @Query("UPDATE episodes SET watchToDate = :date WHERE id = :episodeId")
    void updateEpisodeDateToWatch(String date, int episodeId);
}
