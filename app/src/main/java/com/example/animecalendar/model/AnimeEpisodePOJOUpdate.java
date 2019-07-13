package com.example.animecalendar.model;

import androidx.room.ColumnInfo;

public class AnimeEpisodePOJOUpdate {
    @ColumnInfo(name = "id")
    private long episodeId;
    @ColumnInfo(name = "viewType")
    private int viewType;
    @ColumnInfo(name = "collapse")
    private int collapse;

    public AnimeEpisodePOJOUpdate(long episodeId, int viewType, int collapse) {
        this.episodeId = episodeId;
        this.viewType = viewType;
        this.collapse = collapse;
    }

    public long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(long episodeId) {
        this.episodeId = episodeId;
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
