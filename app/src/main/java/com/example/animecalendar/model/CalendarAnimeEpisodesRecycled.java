package com.example.animecalendar.model;

public class CalendarAnimeEpisodesRecycled {

    private int viewtype;
    private int collapse;
    public static final int COLLAPSE_TITLE = 0;
    public static final int EXPAND_TITLE = 1;
    public static final int DUMMY_COLLAPSE = -1;

//    public CalendarAnimeEpisodesRecycled(int animeId, String animeTitle, int episodeId, String title,
//                                         int length, int number, String watchToDate, int wasWatched) {
//        super(animeId, animeTitle, episodeId, title, length, number, watchToDate, wasWatched);
//    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }

    public int getCollapse() {
        return collapse;
    }

    public void setCollapse(int collapse) {
        this.collapse = collapse;
    }
}
