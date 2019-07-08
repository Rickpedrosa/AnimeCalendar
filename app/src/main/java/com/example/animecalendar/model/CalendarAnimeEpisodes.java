package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class CalendarAnimeEpisodes {
    @ColumnInfo(name = "animeId")
    private int animeId;
    @ColumnInfo(name = "episodeId")
    private int episodeId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "length")
    private int length;
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "watchToDate")
    private String watchToDate;
    @ColumnInfo(name = "wasWatched")
    private int wasWatched;

    public CalendarAnimeEpisodes(int animeId, int episodeId, String title, int length, int number, String watchToDate, int wasWatched) {
        this.animeId = animeId;
        this.episodeId = episodeId;
        this.title = title;
        this.length = length;
        this.number = number;
        this.watchToDate = watchToDate;
        this.wasWatched = wasWatched;
    }

    public int getAnimeId() {
        return animeId;
    }

    public void setAnimeId(int animeId) {
        this.animeId = animeId;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getWatchToDate() {
        return watchToDate;
    }

    public void setWatchToDate(String watchToDate) {
        this.watchToDate = watchToDate;
    }

    public int getWasWatched() {
        return wasWatched;
    }

    public void setWasWatched(int wasWatched) {
        this.wasWatched = wasWatched;
    }
}
