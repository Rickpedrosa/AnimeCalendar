package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class AnimeEpisodeDates {
    @ColumnInfo(name = "watchToDate")
    private String date;
    @ColumnInfo(name = "epsCount")
    private int epsCount;

    public AnimeEpisodeDates(String date, int epsCount) {
        this.date = date;
        this.epsCount = epsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEpsCount() {
        return epsCount;
    }

    public void setEpsCount(int epsCount) {
        this.epsCount = epsCount;
    }
}
