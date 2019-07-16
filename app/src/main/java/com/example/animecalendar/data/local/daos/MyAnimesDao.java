package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.CalendarAnime;

import java.util.List;

@Dao
public interface MyAnimesDao {

    //FROM anime ani LEFT JOIN episodes ep ON ani.id = ep.animeId AND ep.wasWatched = 1 // COUNT(ep.id) AS episodiosVistos
    //TODO No devuelve bien los episodios vistos
    @Query("SELECT ani.id AS id, ani.canonicalTitle AS canonicalTitle, ani.status AS status, ani.tinyPosterImage AS poster, ani.episodeCount AS epCount " +
            " FROM anime ani INNER JOIN episodes eptwo ON ani.id = eptwo.animeId" +
            " GROUP BY ani.id " +
            " HAVING ani.episodeCount = COUNT(eptwo.id) " +
            " ORDER BY ani.averageRating DESC")
    LiveData<List<AnimesForSeries>> getAnimesToExpose();

    //SELECT ani.id AS id, ani.canonicalTitle AS canonicalTitle, ani.status AS status, ani.tinyPosterImage AS poster, ani.episodeCount AS epCount, COUNT(ep.id) AS epsWatched
    //FROM anime ani LEFT JOIN episodes ep ON ani.id = ep.animeId AND ep.wasWatched = 1
    //GROUP BY ani.id
    //ORDER BY ani.averageRating DESC

    @Query("SELECT ani.id AS id, ani.canonicalTitle AS canonicalTitle, ani.episodeCount AS epCount," +
            " COUNT(ep.id) AS epsWatched" +
            " FROM anime ani LEFT JOIN episodes ep ON ani.id = ep.animeId AND ep.wasWatched = 1 " +
            " WHERE ani.status LIKE 'following'" +
            " GROUP BY ani.id ORDER BY ani.averageRating DESC")
    LiveData<List<CalendarAnime>> getAnimesToExposeForCalendar();

    @Query("SELECT * FROM anime WHERE id = :id")
    LiveData<MyAnime> getAnimeForDetail(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addAnime(MyAnime myAnime);

    @Query("DELETE FROM anime WHERE id = :id")
    void deleteAnime(int id);

    @Query("UPDATE anime SET status = :status WHERE id = :animeId")
    void updateAnimeStatus(String status, int animeId);
}
