package com.example.animecalendar.model;

public class MyAnimeEpisodesDailyList {
    private long id;
    private long animeId;
    private String canonicalTitle;
    private int number;
    private String thumbnail;
    private int wasWatched; //1 or 0 (boolean)
    private String animeTitle;

    public MyAnimeEpisodesDailyList(long id, long animeId, String canonicalTitle, int number, String thumbnail, int wasWatched, String animeTitle) {
        this.id = id;
        this.animeId = animeId;
        this.canonicalTitle = canonicalTitle;
        this.number = number;
        this.thumbnail = thumbnail;
        this.wasWatched = wasWatched;
        this.animeTitle = animeTitle;
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

    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }
}
