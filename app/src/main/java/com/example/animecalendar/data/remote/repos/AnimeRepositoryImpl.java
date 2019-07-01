package com.example.animecalendar.data.remote.repos;

import com.example.animecalendar.data.remote.pojos.anime.Anime;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.services.AnimeService;

import io.reactivex.Observable;
import retrofit2.Response;


public class AnimeRepositoryImpl implements AnimeRepository {
    @Override
    public Observable<Response<Anime>> getAnime(String id) {
        return AnimeService.getInstance().getAnimeRepository().getAnime(id);
    }

    @Override
    public Observable<Response<AnimeEpisode>> getAnimeEpisodes(String id, int offset, int limit) {
        return AnimeService.getInstance().getAnimeRepository().getAnimeEpisodes(
                id,
                offset,
                AnimeService.LIMIT);
    }

    @Override
    public Observable<AnimeEpisode> getAnimeEpisodesV2(String id, int offset, int limit) {
        return AnimeService.getInstance().getAnimeRepository().getAnimeEpisodesV2(
                id,
                offset,
                AnimeService.LIMIT);
    }
}
