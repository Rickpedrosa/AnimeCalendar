package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class AnimeEpisodeDateUpdatePOJO {
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "watchToDate")
    private String newDate;

    public AnimeEpisodeDateUpdatePOJO(long id, String newDate) {
        this.id = id;
        this.newDate = newDate;
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
}
