package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

@Dao
public interface MyAnimesEpisodesDao {

    @Query("SELECT id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate  " +
            "FROM episodes WHERE animeId = :id ORDER BY number")
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEpisode(MyAnimeEpisode episode);

    @Update()
    void updateEpisode(MyAnimeEpisode myAnimeEpisode);
}
