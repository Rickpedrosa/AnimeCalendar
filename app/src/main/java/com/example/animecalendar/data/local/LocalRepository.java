package com.example.animecalendar.data.local;

import androidx.lifecycle.LiveData;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.model.MyAnimeEpisodesList;

import java.util.List;

public interface LocalRepository {

    String STATUS_FINISHED = "finished";
    String STATUS_CURRENT = "current";
    String STATUS_FOLLOWING = "following";
    String STATUS_COMPLETED = "completed";

    LiveData<List<AnimesForSeries>> getAnimesToExpose();

    LiveData<MyAnime> getAnimeForDetail(int id);

    void addAnime(MyAnime myAnime);

    void deleteAnime(int id);

    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);

    void addEpisodes(List<MyAnimeEpisode> episodes);

    void updateEpisode(MyAnimeEpisode myAnimeEpisode);

    void updateAnimeStatus(String status, int animeId);
    LiveData<List<CalendarAnimeEpisodes>> getAnimeEpisodesForCalendar();

}
