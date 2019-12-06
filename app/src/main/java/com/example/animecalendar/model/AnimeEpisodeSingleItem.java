package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class AnimeEpisodeSingleItem {
    @ColumnInfo(name = "canonicalTitle")
    private String canonicalTitle;
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;
    @ColumnInfo(name = "synopsis")
    private String synopsis;

    public AnimeEpisodeSingleItem(String canonicalTitle, String thumbnail, String synopsis) {
        this.canonicalTitle = canonicalTitle;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public void setCanonicalTitle(String canonicalTitle) {
        this.canonicalTitle = canonicalTitle;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
