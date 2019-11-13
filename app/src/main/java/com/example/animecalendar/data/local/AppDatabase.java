package com.example.animecalendar.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.animecalendar.data.local.daos.MyAnimeCharactersDao;
import com.example.animecalendar.data.local.daos.MyAnimesDao;
import com.example.animecalendar.data.local.daos.MyAnimesEpisodesDao;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;

@Database(entities = {MyAnime.class, MyAnimeEpisode.class, MyAnimeCharacter.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "animeCalendar";
    private static volatile AppDatabase instance;

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(), AppDatabase.class,
                DATABASE_NAME)
                .build();
    }

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }
        }
        return instance;
    }

    public abstract MyAnimesDao myAnimesDao();
    public abstract MyAnimesEpisodesDao myAnimesEpisodesDao();
    public abstract MyAnimeCharactersDao myAnimeCharactersDao();
}