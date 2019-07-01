package com.example.animecalendar.model;

public class MyAnimeEpisodesList {

    private long id;
    private long animeId;
    private String canonicalTitle;
    private int number;
    private String thumbnail;
    private int wasWatched; //1 or 0 (boolean)
    private String watchToDate; //date

    public MyAnimeEpisodesList(long id, long animeId, String canonicalTitle, int number, String thumbnail, int wasWatched, String watchToDate) {
        this.id = id;
        this.animeId = animeId;
        this.canonicalTitle = canonicalTitle;
        this.number = number;
        this.thumbnail = thumbnail;
        this.wasWatched = wasWatched;
        this.watchToDate = watchToDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnimeId() {
        return animeId;
    }

    public void setAnimeId(long animeId) {
        this.animeId = animeId;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public void setCanonicalTitle(String canonicalTitle) {
        this.canonicalTitle = canonicalTitle;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getWasWatched() {
        return wasWatched;
    }

    public void setWasWatched(int wasWatched) {
        this.wasWatched = wasWatched;
    }

    public String getWatchToDate() {
        return watchToDate;
    }

    public void setWatchToDate(String watchToDate) {
        this.watchToDate = watchToDate;
    }
}
