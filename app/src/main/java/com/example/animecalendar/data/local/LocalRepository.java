package com.example.animecalendar.data.local;

import androidx.lifecycle.LiveData;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.model.MyAnimeEpisodesDailyList;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

public interface LocalRepository {

    String STATUS_FINISHED = "finished";
    String STATUS_CURRENT = "current";
    String STATUS_FOLLOWING = "following";
    String STATUS_COMPLETED = "completed";
    String WATCH_DATE_NULL = "-";
    String WATCH_DATE_DONE = "Watched";
    int WATCHED = 1;
    int NOT_WATCHED = 0;

    LiveData<List<AnimesForSeries>> getAnimesToExpose();

    LiveData<MyAnime> getAnimeForDetail(int id);

    void addAnime(MyAnime myAnime);

    void deleteAnime(int id);

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    void addEpisodes(List<MyAnimeEpisode> episodes);

    void updateAnimeStatus(String status, int animeId);

    LiveData<List<CalendarAnime>> getAnimesToExposeForCalendar();

    LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getAnimeEpisodesToAssignDate(int animeId);

    void updateEpisodeDateToWatchPojoVersion(List<AnimeEpisodeDateUpdatePOJO> episodes);

    LiveData<List<AnimesForSeries>> getAnimesToExposeByCategory(String category);

    LiveData<List<AnimeEpisodeDates>> getDatesFromWatchableEpisodes();

    void updateEpisodeStatusAndDatePOJO(AnimeEpDateStatusPOJO episode);

    LiveData<List<AnimesForSeries>> getAnimesToExposeBySearchQuery(String query);

    LiveData<List<String>> getTodayItems(String today);

    LiveData<List<MyAnimeEpisodesDailyList>> getEpisodesOfTheDay(String day);

    void addEpisodesWithReplace(List<MyAnimeEpisode> episodes);

    LiveData<List<MyAnimeCharacter>> getAnimeCharacters(long animeId);

    void addAnimeCharacters(List<MyAnimeCharacter> characters);

    void deleteAnimeCharacters(long animeId);

    LiveData<List<MyAnimeCharacter>> getAnimeCharactersByQuery(long animeId, String query);

    LiveData<Integer> checkIfAnimeHasCharacters(long animeId);

    void updateEpisodeStatus(int value, int episodeId);
}
