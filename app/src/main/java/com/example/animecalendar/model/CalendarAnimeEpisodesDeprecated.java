package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class CalendarAnimeEpisodesDeprecated {
    @ColumnInfo(name = "animeId")
    private int animeId;
    @ColumnInfo(name = "animeTitle")
    private String animeTitle;
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "canonicalTitle")
    private String episodeTitle;
    @ColumnInfo(name = "length")
    private int length;
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "watchToDate")
    private String watchToDate;
    @ColumnInfo(name = "wasWatched")
    private int wasWatched;
    @ColumnInfo(name = "viewType")
    private int viewType;
    @ColumnInfo(name = "collapse")
    private int collapse;

    public CalendarAnimeEpisodesDeprecated(int animeId, String animeTitle, int id, String episodeTitle,
                                           int length, int number, String watchToDate, int wasWatched, int viewType, int collapse) {
        this.animeId = animeId;
        this.animeTitle = animeTitle;
        this.id = id;
        this.episodeTitle = episodeTitle;
        this.length = length;
        this.number = number;
        this.watchToDate = watchToDate;
        this.wasWatched = wasWatched;
        this.viewType = viewType;
        this.collapse = collapse;
    }

    public int getAnimeId() {
        return animeId;
    }

    public void setAnimeId(int animeId) {
        this.animeId = animeId;
    }

    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
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

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getCollapse() {
        return collapse;
    }

    public void setCollapse(int collapse) {
        this.collapse = collapse;
    }
}
