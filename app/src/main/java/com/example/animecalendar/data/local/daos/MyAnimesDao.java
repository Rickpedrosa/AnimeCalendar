package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.model.AnimesForSeries;

import java.util.List;

@Dao
public interface MyAnimesDao {

    @Query("SELECT ani.id AS id, ani.canonicalTitle AS canonicalTitle, ani.tinyPosterImage AS poster, ani.episodeCount AS epCount," +
            " COUNT(ep.id) AS epsWatched" +
            " FROM anime ani LEFT JOIN episodes ep ON ani.id = ep.animeId AND ep.wasWatched = 1" +
            " GROUP BY ani.id")
    LiveData<List<AnimesForSeries>> getAnimesToExpose();

    @Query("SELECT * FROM anime WHERE id = :id")
    LiveData<MyAnime> getAnimeForDetail(int id);

    @Insert()
    void addAnime(MyAnime myAnime);

    @Delete
    void deleteAnime(MyAnime myAnime);

    //TODO Update anime app status?
}
