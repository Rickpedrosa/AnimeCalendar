package com.example.animecalendar.data.remote.repos;

import com.example.animecalendar.data.remote.pojos.anime.Anime;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.AnimeCharacterIDs;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.animelist.AnimationList;
import com.example.animecalendar.data.remote.services.AnimeService;

import io.reactivex.Observable;
import retrofit2.Response;


public class AnimeRepositoryImpl implements AnimeRepository {
    @Override
    public Observable<Response<Anime>> getAnime(String id) {
        return AnimeService.getInstance().getAnimeRepository().getAnime(id);
    }

    @Override
    public Observable<Response<AnimationList>> getAnimesByFilterText(String title, int limit) {
        return AnimeService.getInstance().getAnimeRepository().getAnimesByFilterText(title, AnimeService.LIMIT);
    }

    @Override
    public Observable<Response<AnimeEpisode>> getAnimeEpisodes(String id, int offset) {
        return AnimeService.getInstance().getAnimeRepository().getAnimeEpisodes(
                id,
                offset);
    }

    @Override
    public Observable<AnimeCharacterIDs> getAnimeCharactersIds(String id) {
        return AnimeService.getInstance().getAnimeRepository().getAnimeCharactersIds(id);
    }

    @Override
    public Observable<AnimeCharacterDetail> getAnimeCharacterDetails(String characterId) {
        return AnimeService.getInstance().getAnimeRepository().getAnimeCharacterDetails(characterId);
    }
}
