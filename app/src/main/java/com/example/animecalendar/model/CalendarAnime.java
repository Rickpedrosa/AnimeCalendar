package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class CalendarAnime {
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "canonicalTitle")
    private String title;
    @ColumnInfo(name = "epCount")
    private int epCount;
    @ColumnInfo(name = "epsWatched")
    private int epWatchedCount;
    @ColumnInfo(name = "tinyPosterImage")
    private String tinyPosterImage;


    public CalendarAnime(int id, String title, int epCount, int epWatchedCount, String tinyPosterImage) {
        this.id = id;
        this.title = title;
        this.epCount = epCount;
        this.epWatchedCount = epWatchedCount;
        this.tinyPosterImage = tinyPosterImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpCount() {
        return epCount;
    }

    public void setEpCount(int epCount) {
        this.epCount = epCount;
    }

    public int getEpWatchedCount() {
        return epWatchedCount;
    }

    public void setEpWatchedCount(int epWatchedCount) {
        this.epWatchedCount = epWatchedCount;
    }

    public String getTinyPosterImage() {
        return tinyPosterImage;
    }

    public void setTinyPosterImage(String tinyPosterImage) {
        this.tinyPosterImage = tinyPosterImage;
    }
}
