package com.example.animecalendar.providers;

import com.example.animecalendar.model.CalendarAnimeEpisodesDeprecated;

public class AnimeEpisodeProvider {

    public static CalendarAnimeEpisodesDeprecated getEpisodeWithViewType(CalendarAnimeEpisodesDeprecated source, int viewType) {
        return new CalendarAnimeEpisodesDeprecated(
                source.getAnimeId(),
                source.getAnimeTitle(),
                source.getId(),
                source.getEpisodeTitle(),
                source.getLength(),
                source.getNumber(),
                source.getWatchToDate(),
                source.getWasWatched(),
                viewType,
                source.getCollapse()
        );
    }

    public static CalendarAnimeEpisodesDeprecated getEpisodeWithCollapse(CalendarAnimeEpisodesDeprecated source, int collapse) {
        return new CalendarAnimeEpisodesDeprecated(
                source.getAnimeId(),
                source.getAnimeTitle(),
                source.getId(),
                source.getEpisodeTitle(),
                source.getLength(),
                source.getNumber(),
                source.getWatchToDate(),
                source.getWasWatched(),
                source.getViewType(),
                collapse
        );
    }

    private AnimeEpisodeProvider() {

    }
}
