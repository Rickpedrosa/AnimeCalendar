package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.model.MyAnimeList;

import java.util.List;

@Dao
public interface MyAnimesDao {

    @Query("SELECT id, canonicalTitle, status, coverImage, episodeCount FROM anime")
    LiveData<List<MyAnimeList>> getAnimesToExpose();

    @Query("SELECT * FROM anime WHERE id = :id")
    LiveData<MyAnime> getAnimeForDetail(int id);

    @Insert()
    void addAnime();

    @Delete
    void deleteAnime();

    //TODO Update anime app status?
}
