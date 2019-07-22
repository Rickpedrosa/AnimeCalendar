package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class AnimeEpDateStatusPOJO {
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "watchToDate")
    private String newDate;
    @ColumnInfo(name = "wasWatched")
    private int status;

    public AnimeEpDateStatusPOJO(long id, String newDate, int status) {
        this.id = id;
        this.newDate = newDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
