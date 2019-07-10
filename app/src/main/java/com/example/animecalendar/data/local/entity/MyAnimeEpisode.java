package com.example.animecalendar.data.local.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@SuppressWarnings("WeakerAccess")
@Entity(tableName = "episodes",
        indices = {@Index(value = "animeId")},
        foreignKeys = @ForeignKey(
                entity = MyAnime.class,
                parentColumns = "id",
                childColumns = "animeId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ))
public class MyAnimeEpisode {

    //SELECT anime.*,

    @PrimaryKey()
    private long id;
    @ColumnInfo(name = "animeId")
    private long animeId;
    @ColumnInfo(name = "canonicalTitle")
    private String canonicalTitle;
    @ColumnInfo(name = "seasonNumber")
    private int seasonNumber;
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "synopsis")
    private String synopsis;
    @ColumnInfo(name = "airDate")
    private String airDate;
    @ColumnInfo(name = "length")
    private int length;
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;
    @ColumnInfo(name = "wasWatched")
    private int wasWatched; //1 or 0 (boolean)
    @ColumnInfo(name = "watchToDate")
    private String watchToDate; //date
    @ColumnInfo(name = "viewType")
    public int viewType;
    @ColumnInfo(name = "collapse")
    public int collapse;

    public MyAnimeEpisode(long id,
                          long animeId,
                          String canonicalTitle,
                          int seasonNumber,
                          int number,
                          String synopsis,
                          String airDate,
                          int length,
                          String thumbnail,
                          int wasWatched,
                          String watchToDate) {
        this.id = id;
        this.animeId = animeId;
        this.canonicalTitle = canonicalTitle;
        this.seasonNumber = seasonNumber;
        this.number = number;
        this.synopsis = synopsis;
        this.airDate = airDate;
        this.length = length;
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

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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
