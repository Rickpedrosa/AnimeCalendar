package com.example.animecalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animecalendar.R;
import com.example.animecalendar.databinding.OuterFragmentCalendarBinding;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.List;

public class CalendarFragment extends Fragment {

    private NavController navController;
    private CalendarFragmentViewAdapter listAdapter;
    private CalendarFragmentViewModel viewModel;
    private OuterFragmentCalendarBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.CALENDAR))
                .get(CalendarFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.outer_fragment_calendar, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeData();
    }

    private void setupViews() {
        setupRecyclerView();
        setupToolbar();
    }


    private void setupToolbar() {
        b.toolbarCalendarFragment.inflateMenu(R.menu.calendar_menu);
        b.toolbarCalendarFragment.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideShowCalendar) {
                navController.navigate(R.id.daysFragment);
                return true;
            }
            return false;
        });
        NavigationUI.setupWithNavController(
                b.toolbarCalendarFragment,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void setupRecyclerView() {
        listAdapter = new CalendarFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> navigateToEpisodes(position));
        b.listEpisodesCalendar
                .setItemAnimator(new DefaultItemAnimator());
        b.listEpisodesCalendar
                .setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listEpisodesCalendar
                .setAdapter(listAdapter);
    }

    private void navigateToEpisodes(int position) {
        navController.navigate(CalendarFragmentDirections
                .actionCalendarFragmentToCalendarEpisodesFragment(listAdapter.getItem(position).getId()));
    }

    private void observeData() {
        viewModel.getAnimeWithEpisodes().observe(getViewLifecycleOwner(), this::submitDataToAdapter);
    }

    private void submitDataToAdapter(List<CalendarAnime> calendarAnimeEpisodes) {
        listAdapter.submitList(calendarAnimeEpisodes);
        b.lblNoAnime.setVisibility(calendarAnimeEpisodes.size() == 0 ?
                View.VISIBLE : View.INVISIBLE);
        updateAnimeStatus(calendarAnimeEpisodes);
    }

    private void updateAnimeStatus(List<CalendarAnime> calendarAnimeEpisodes) {
        for (int i = 0; i < calendarAnimeEpisodes.size(); i++) {
            if (calendarAnimeEpisodes.get(i).getEpWatchedCount() ==
                    calendarAnimeEpisodes.get(i).getEpCount()) {
                viewModel.updateAnimeStatus(calendarAnimeEpisodes.get(i).getId());
            }
        }
    }
}
