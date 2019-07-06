package com.example.animecalendar.data.local.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.animecalendar.data.local.LocalRepository;

@Entity(tableName = "anime",
        indices = {@Index(value = "canonicalTitle")})
public class MyAnime {
    @PrimaryKey()
    private long id;
    @ColumnInfo(name = "synopsis")
    private String synopsis;
    @ColumnInfo(name = "canonicalTitle")
    private String canonicalTitle;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "averageRating")
    private float averageRating;
    @ColumnInfo(name = "tinyPosterImage")
    private String tinyPosterImage;
    @ColumnInfo(name = "mediumPosterImage")
    private String mediumPosterImage;
    @ColumnInfo(name = "coverImage")
    private String coverImage; //tiny
    @ColumnInfo(name = "episodeCount")
    private int episodeCount;

    public MyAnime(long id,
                   String synopsis,
                   String canonicalTitle,
                   String status,
                   float averageRating,
                   String tinyPosterImage,
                   String mediumPosterImage,
                   String coverImage,
                   int episodeCount) {
        this.id = id;
        this.synopsis = synopsis;
        this.canonicalTitle = canonicalTitle;
        this.status = status;
        this.averageRating = averageRating;
        this.tinyPosterImage = tinyPosterImage;
        this.mediumPosterImage = mediumPosterImage;
        this.coverImage = coverImage;
        this.episodeCount = episodeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getTinyPosterImage() {
        return tinyPosterImage;
    }

    public void setTinyPosterImage(String tinyPosterImage) {
        this.tinyPosterImage = tinyPosterImage;
    }

    public String getMediumPosterImage() {
        return mediumPosterImage;
    }

    public void setMediumPosterImage(String mediumPosterImage) {
        this.mediumPosterImage = mediumPosterImage;
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
