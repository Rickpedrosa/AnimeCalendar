package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

@Dao
public interface MyAnimesEpisodesDao {

    @Query("SELECT id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate  " +
            "FROM episodes WHERE animeId = :id ORDER BY number")
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    @Query("SELECT ani.id AS animeId, ani.canonicalTitle AS animeTitle, ep.id AS episodeId, " +
            "ep.canonicalTitle AS episodeTitle, ep.length, ep.number, ep.watchToDate, ep.wasWatched, " +
            "ep.viewType AS viewType, ep.collapse AS collapse " +
            "FROM episodes ep INNER JOIN anime ani ON ep.animeId = ani.id " +
            "WHERE ani.status LIKE 'following'" +
            "GROUP BY ani.id, ep.number " +
            "ORDER BY ani.id, ep.number")
    LiveData<List<CalendarAnimeEpisodes>> getAnimeEpisodesForCalendar();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addEpisodes(List<MyAnimeEpisode> episodes);

    @Query("UPDATE episodes SET wasWatched = :value WHERE id = :episodeId")
    void updateEpisodeStatus(int value, int episodeId);

    @Query("UPDATE episodes SET viewType = :viewType WHERE id = :episodeId")
    void updateEpisodeViewType(int viewType, int episodeId);

    @Query("UPDATE episodes SET collapse = :collapse WHERE id = :episodeId")
    void updateEpisodeCollapse(int collapse, int episodeId);
}
