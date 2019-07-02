package com.example.animecalendar.providers;

import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class RXJavaProvider {

    public static Observable<AnimeEpisode> episodeObservableFlatMapped(Observable<Response<AnimeEpisode>> observable) {
        return observable.flatMap((Function<Response<AnimeEpisode>,
                ObservableSource<AnimeEpisode>>) animeEpisodeResponse
                -> Observable.just(Objects.requireNonNull(animeEpisodeResponse.body())));
    }

    private RXJavaProvider() {
    }
}
