package com.example.animecalendar.provider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.animecalendar.base.vm.ViewModelFragmentFactory;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.data.local.LocalRepositoryImpl;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.ui.calendar.CalendarFragment;
import com.example.animecalendar.ui.detail_anime.DetailAnimeFragment;
import com.example.animecalendar.ui.detail_episode.DetailAnimeEpisodeFragment;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.ui.main.MainActivityViewModelFactory;
import com.example.animecalendar.ui.search.SearchFragment;
import com.example.animecalendar.ui.series.MyAnimeSeriesFragment;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class Providers {

    public enum FRAGMENTS {
        SEARCH, CALENDAR, SERIES, DETAIL_ANIME, DETAIL_EPISODE
    }

    public static ViewModelFragmentFactory viewModelFragmentFactory(Fragment fragment, FRAGMENTS enumFragment) {
        return new ViewModelFragmentFactory(ViewModelProviders.of(fragment,
                new MainActivityViewModelFactory(fragment.requireActivity().getApplication(),
                        AppDatabase.getInstance(fragment.requireContext())))
                .get(MainActivityViewModel.class), enumFragment);
    }

    public static Observable<AnimeEpisode> episodeObservableFlatMapped(Observable<Response<AnimeEpisode>> observable) {
        return observable.flatMap((Function<Response<AnimeEpisode>,
                ObservableSource<AnimeEpisode>>) animeEpisodeResponse
                -> Observable.just(Objects.requireNonNull(animeEpisodeResponse.body())));
    }

    private Providers() {

    }
}
