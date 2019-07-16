package com.example.animecalendar.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class AnimesForSeries {
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "canonicalTitle")
    private String title;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "poster")
    private String poster;
    @ColumnInfo(name = "epCount")
    private int epCount;
    @Ignore
    @ColumnInfo(name = "epsWatched")
    private int epWatchedCount;

    public AnimesForSeries(int id, String title, String status, String poster, int epCount) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.poster = poster;
        this.epCount = epCount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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
}
