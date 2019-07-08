package com.example.animecalendar.model;

public class CalendarAnimeEpisodesRecycled extends CalendarAnimeEpisodes {

    private int viewtype;

    public CalendarAnimeEpisodesRecycled(int animeId, String animeTitle, int episodeId, String title,
                                         int length, int number, String watchToDate, int wasWatched) {
        super(animeId, animeTitle, episodeId, title, length, number, watchToDate, wasWatched);
    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }
}
