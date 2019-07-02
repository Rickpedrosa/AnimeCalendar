package com.example.animecalendar.data.remote.repos;

import com.example.animecalendar.data.remote.pojos.anime.Anime;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.data.remote.pojos.animelist.AnimationList;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnimeRepository {

    @GET("anime/{id}")
    Observable<Response<Anime>> getAnime(@Path("id") String id);

    @Headers({"Accept: application/vnd.api+json",
            "Content-Type: application/vnd.api+json"})
    @GET("anime")
    Observable<Response<AnimationList>> getAnimesByFilterText(@Query("filter[text]") String title, @Query("page[limit]") int limit);

    //https://kitsu.io/api/edge/anime/21/episodes

    @GET("anime/{id}/episodes")
    Observable<Response<AnimeEpisode>> getAnimeEpisodes(@Path("id") String id, @Query("page[offset]") int offset, @Query("page[limit]") int limit);

    @GET("anime/{id}/episodes")
    Observable<AnimeEpisode> getAnimeEpisodesV2(@Path("id") String id, @Query("page[offset]") int offset, @Query("page[limit]") int limit);
}
