package com.example.animecalendar.provider;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.animecalendar.base.vm.ViewModelFragmentFactory;
import com.example.animecalendar.data.local.LocalRepositoryImpl;
import com.example.animecalendar.ui.calendar.CalendarFragment;
import com.example.animecalendar.ui.detail_anime.DetailAnimeFragment;
import com.example.animecalendar.ui.detail_episode.DetailAnimeEpisodeFragment;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.ui.main.MainActivityViewModelFactory;
import com.example.animecalendar.ui.search.SearchFragment;
import com.example.animecalendar.ui.series.MyAnimeSeriesFragment;

public class Providers {

    public static final String SEARCH_FRAGMENT_CLASS_NAME = SearchFragment.class.getSimpleName();
    public static final String CALENDAR_FRAGMENT_CLASS_NAME = CalendarFragment.class.getSimpleName();
    public static final String ANIME_SERIES_FRAGMENT_CLASS_NAME = MyAnimeSeriesFragment.class.getSimpleName();
    public static final String DETAIL_ANIME_CLASS_NAME = DetailAnimeFragment.class.getSimpleName();
    public static final String DETAIL_EPISODE_CLASS_NAME = DetailAnimeEpisodeFragment.class.getSimpleName();

    public static ViewModelFragmentFactory viewModelFragmentFactory(FragmentActivity f, String className) {
        return new ViewModelFragmentFactory(ViewModelProviders.of(f,
                new MainActivityViewModelFactory(f.getApplication(), new LocalRepositoryImpl()))
                .get(MainActivityViewModel.class), className);
    }

    private Providers() {

    }
}
