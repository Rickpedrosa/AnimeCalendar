package com.example.animecalendar.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recycledEpisodes",
        indices = @Index(value = "animeId"))
public class MyRecycledAnimeEpisodes {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "animeId")
    private int animeId;
    @ColumnInfo(name = "animeTitle")
    private String animeTitle;
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
    @ColumnInfo(name = "viewType")
    private int viewType;
    @ColumnInfo(name = "collapse")
    private int collapse;

    public MyRecycledAnimeEpisodes(int id, int animeId, String animeTitle, int episodeId, String title,
                                   int length, int number, String watchToDate, int wasWatched, int viewType, int collapse) {
        this.id = id;
        this.animeId = animeId;
        this.animeTitle = animeTitle;
        this.episodeId = episodeId;
        this.title = title;
        this.length = length;
        this.number = number;
        this.watchToDate = watchToDate;
        this.wasWatched = wasWatched;
        this.viewType = viewType;
        this.collapse = collapse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
