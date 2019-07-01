package com.example.animecalendar.model;

public class MyAnimeList {

    private long id;
    private String canonicalTitle;
    private String status;
    private String coverImage;
    private int episodeCount;

    public MyAnimeList(long id, String canonicalTitle, String status, String coverImage, int episodeCount) {
        this.id = id;
        this.canonicalTitle = canonicalTitle;
        this.status = status;
        this.coverImage = coverImage;
        this.episodeCount = episodeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public void setCanonicalTitle(String canonicalTitle) {
        this.canonicalTitle = canonicalTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }
}
