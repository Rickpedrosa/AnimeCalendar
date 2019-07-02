package com.example.animecalendar.data.remote.services;

import com.example.animecalendar.data.remote.repos.AnimeRepository;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeService {
    private static AnimeService INSTANCE;
    private final AnimeRepository animeRepository;
    public static final int LIMIT = 20;
    public static final int PLUS_OFFSET = 20;

    private AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public static AnimeService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnimeService(buildInstance());
        }
        return INSTANCE;
    }

    private static AnimeRepository buildInstance() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/vnd.api+json")
                    .addHeader("Content-Type", "application/vnd.api+json")
                    .build();
            return chain.proceed(request);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io/api/edge/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(AnimeRepository.class);
    }

    public AnimeRepository getAnimeRepository() {
        return animeRepository;
    }
}
