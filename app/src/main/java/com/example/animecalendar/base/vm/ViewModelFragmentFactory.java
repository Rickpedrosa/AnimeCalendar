package com.example.animecalendar.base.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.animecalendar.provider.Providers;
import com.example.animecalendar.ui.calendar.CalendarFragmentViewModel;
import com.example.animecalendar.ui.detail_anime.DetailAnimeFragmentViewModel;
import com.example.animecalendar.ui.detail_episode.DetailAnimeEpisodeFragmentViewModel;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.ui.search.SearchFragmentViewModel;
import com.example.animecalendar.ui.series.MyAnimeSeriesFragmentViewModel;

public class ViewModelFragmentFactory implements ViewModelProvider.Factory {

    private final MainActivityViewModel mainActivityViewModel;
    private String className;

    public ViewModelFragmentFactory(MainActivityViewModel mainActivityViewModel, String className) {
        this.mainActivityViewModel = mainActivityViewModel;
        this.className = className;
    }

    //TODO CAMBIAR ESTA VAINA
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (Providers.SEARCH_FRAGMENT_CLASS_NAME.equals(className)) {
            return (T) new SearchFragmentViewModel(mainActivityViewModel);
        } else if (Providers.ANIME_SERIES_FRAGMENT_CLASS_NAME.equals(className)) {
            return (T) new MyAnimeSeriesFragmentViewModel();
        } else if (Providers.DETAIL_ANIME_CLASS_NAME.equals(className)) {
            return (T) new DetailAnimeFragmentViewModel();
        } else if (Providers.DETAIL_EPISODE_CLASS_NAME.equals(className)) {
            return (T) new DetailAnimeEpisodeFragmentViewModel();
        } else if (Providers.CALENDAR_FRAGMENT_CLASS_NAME.equals(className)) {
            return (T) new CalendarFragmentViewModel();
        }
        return null;
    }
}
