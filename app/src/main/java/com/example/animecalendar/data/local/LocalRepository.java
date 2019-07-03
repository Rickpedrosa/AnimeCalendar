package com.example.animecalendar.data.local;

import androidx.lifecycle.LiveData;

import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.local.entity.MyAnimeEpisode;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.model.MyAnimeList;

import java.util.List;

public interface LocalRepository {
    LiveData<List<MyAnimeList>> getAnimesToExpose();
    LiveData<MyAnime> getAnimeForDetail(int id);
    void addAnime(MyAnime myAnime);
    void deleteAnime(MyAnime myAnime);
    LiveData<List<MyAnimeEpisodesList>> getAnimeEpisodes(int id);
    void addEpisodes(List<MyAnimeEpisode> episodes);
    void updateEpisode(MyAnimeEpisode myAnimeEpisode);
}
