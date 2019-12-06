package com.example.animecalendar.providers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.animecalendar.base.vm.ViewModelFragmentFactory;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.ui.main.MainActivityViewModelFactory;

public class VMProvider {

    public enum FRAGMENTS {
        SEARCH, CALENDAR, SERIES, DETAIL_ANIME, DETAIL_ITEM, CALENDAR_EPISODES, ASSIGNMENT,
        DAYS, DAYS_EPISODE, CHARACTERS, EPISODES;

        private int animeId;

        FRAGMENTS() {
        }

        public int getAnimeId() {
            return animeId;
        }

        public void setAnimeId(int animeId) {
            this.animeId = animeId;
        }
    }

    public static ViewModelFragmentFactory viewModelFragmentFactory(Fragment fragment, FRAGMENTS enumFragment) {
        return new ViewModelFragmentFactory(androidx.lifecycle.ViewModelProviders.of(fragment,
                new MainActivityViewModelFactory(fragment.requireActivity().getApplication(),
                        AppDatabase.getInstance(fragment.requireContext())))
                .get(MainActivityViewModel.class), enumFragment);
    }

    public static ViewModelFragmentFactory viewModelFragmentFactory(FragmentActivity activity, FRAGMENTS enumFragment) {
        return new ViewModelFragmentFactory(androidx.lifecycle.ViewModelProviders.of(activity,
                new MainActivityViewModelFactory(activity.getApplication(),
                        AppDatabase.getInstance(activity.getApplicationContext())))
                .get(MainActivityViewModel.class), enumFragment);
    }

    private VMProvider() {

    }
}
