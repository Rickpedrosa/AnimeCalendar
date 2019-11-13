package com.example.animecalendar.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.animecalendar.data.local.entity.MyAnimeCharacter;

import java.util.List;

@Dao
public interface MyAnimeCharactersDao {

    @Query("SELECT id, animeId, canonicalName, description, image FROM character " +
            "WHERE animeId = :animeId")
    LiveData<List<MyAnimeCharacter>> getAnimeCharacters(long animeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAnimeCharacters(List<MyAnimeCharacter> characters);

    @Query("DELETE FROM character WHERE animeId = :animeId")
    void deleteAnimeCharacters(long animeId);
}
