package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class MyAnimeEpisodeListWithAnimeTitle extends MyAnimeEpisodesList {
    @ColumnInfo(name = "animeTitle")
    private String animeTitle;

    public MyAnimeEpisodeListWithAnimeTitle(long id, long animeId, String canonicalTitle, int number, String thumbnail, int wasWatched, String watchToDate, String animeTitle) {
        super(id, animeId, canonicalTitle, number, thumbnail, wasWatched, watchToDate);
        this.animeTitle = animeTitle;
    }

    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }
}
