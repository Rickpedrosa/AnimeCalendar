package com.example.animecalendar.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "character",
        indices = {@Index(value = "canonicalName")},
        foreignKeys = @ForeignKey(
                entity = MyAnime.class,
                parentColumns = "id",
                childColumns = "animeId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ))
public class MyAnimeCharacter {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "animeId")
    private long animeId;
    @ColumnInfo(name = "canonicalName")
    private String canonicalName;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "image")
    private String image;

    public MyAnimeCharacter(long id, long animeId, String canonicalName, String description, String image) {
        this.id = id;
        this.animeId = animeId;
        this.canonicalName = canonicalName;
        this.description = description;
        this.image = image;
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

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
