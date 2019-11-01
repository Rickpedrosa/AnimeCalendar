package com.example.animecalendar.data.remote.repos;

import com.example.animecalendar.data.remote.pojos.anime.Anime;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.data.remote.pojos.anime_character_ids.AnimeCharacterIDs;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.animelist.AnimationList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnimeRepository {

    @GET("anime/{id}")
    Observable<Response<Anime>> getAnime(@Path("id") String id);

    @GET("anime")
    Observable<Response<AnimationList>> getAnimesByFilterText(@Query("filter[text]") String title, @Query("page[limit]") int limit);

    //https://kitsu.io/api/edge/anime/21/episodes

    @GET("anime/{id}/episodes")
    Observable<Response<AnimeEpisode>> getAnimeEpisodes(@Path("id") String id, @Query("page[offset]") int offset, @Query("page[limit]") int limit);

    @GET("anime/{id}/episodes")
    Observable<AnimeEpisode> getAnimeEpisodesV2(@Path("id") String id, @Query("page[offset]") int offset, @Query("page[limit]") int limit);

    @GET("anime/{id}/relationships/characters")
    Observable<AnimeCharacterIDs> getAnimeCharactersIds(@Path("id") String id);

    @GET("characters/{id}")
    Observable<AnimeCharacterDetail> getAnimeCharacterDetails(@Path("id") String characterId);
}
