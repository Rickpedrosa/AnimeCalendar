package com.example.animecalendar.base.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.ui.assignment.AssignmentFragmentViewModel;
import com.example.animecalendar.ui.calendar.CalendarFragmentViewModel;
import com.example.animecalendar.ui.calendar_episodes.CalendarEpisodesFragmentViewModel;
import com.example.animecalendar.ui.characters.CharactersFragmentViewModel;
import com.example.animecalendar.ui.days.DaysFragmentViewModel;
import com.example.animecalendar.ui.days_episodes.DaysEpisodesFragmentViewModel;
import com.example.animecalendar.ui.detail_anime.DetailAnimeFragmentViewModel;
import com.example.animecalendar.ui.detail_item.DetailItemFragmentViewModel;
import com.example.animecalendar.ui.episodes.EpisodesFragmentViewModel;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.ui.search.SearchFragmentViewModel;
import com.example.animecalendar.ui.series.MyAnimeSeriesFragmentViewModel;

public class ViewModelFragmentFactory implements ViewModelProvider.Factory {

    private final MainActivityViewModel mainActivityViewModel;
    private VMProvider.FRAGMENTS enumFragment;

    public ViewModelFragmentFactory(MainActivityViewModel mainActivityViewModel, VMProvider.FRAGMENTS enumFragment) {
        this.mainActivityViewModel = mainActivityViewModel;
        this.enumFragment = enumFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        switch (enumFragment) {
            case SEARCH:
                return (T) new SearchFragmentViewModel(mainActivityViewModel);
            case SERIES:
                return (T) new MyAnimeSeriesFragmentViewModel(mainActivityViewModel);
            case CALENDAR:
                return (T) new CalendarFragmentViewModel(mainActivityViewModel);
            case DETAIL_ANIME:
                return (T) new DetailAnimeFragmentViewModel(mainActivityViewModel);
            case DETAIL_ITEM:
                return (T) new DetailItemFragmentViewModel(mainActivityViewModel);
            case CALENDAR_EPISODES:
                return (T) new CalendarEpisodesFragmentViewModel(mainActivityViewModel);
            case ASSIGNMENT:
                return (T) new AssignmentFragmentViewModel(mainActivityViewModel);
            case DAYS:
                return (T) new DaysFragmentViewModel(mainActivityViewModel);
            case DAYS_EPISODE:
                return (T) new DaysEpisodesFragmentViewModel(mainActivityViewModel);
            case CHARACTERS:
                return (T) new CharactersFragmentViewModel(mainActivityViewModel, this.enumFragment.getAnimeId());
            case EPISODES:
                return (T) new EpisodesFragmentViewModel(mainActivityViewModel, this.enumFragment.getAnimeId());
            default:
                return null;
        }

    }
}