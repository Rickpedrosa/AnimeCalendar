package com.example.animecalendar.ui.calendar_episodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animecalendar.R;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.Objects;

import static com.example.animecalendar.data.local.LocalRepository.NOT_WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCH_DATE_DONE;
import static com.example.animecalendar.data.local.LocalRepository.WATCH_DATE_NULL;

public class CalendarEpisodesFragment extends Fragment {

    private FragmentCalendarEpisodeBinding b;
    private int animeId;
    private CalendarEpisodesFragmentViewAdapter listAdapter;
    private CalendarEpisodesFragmentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.CALENDAR_EPISODES))
                .get(CalendarEpisodesFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_episode, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        obtainArguments();
        setupRecyclerView();
        setupToolbar();
        observeData();
    }

    private void setupRecyclerView() {
        listAdapter = new CalendarEpisodesFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> updateEpisode(position));
        b.includeCalendarEpisodeContent.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.includeCalendarEpisodeContent.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.includeCalendarEpisodeContent.listEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.includeCalendarEpisodeContent.listEpisodes.setAdapter(listAdapter);
    }

    private void updateEpisode(int position) {
        if (listAdapter.getItem(position).getWasWatched() == 0) {
            viewModel.updateEpisodeStatus(WATCHED, (int) listAdapter.getItem(position).getId());
            viewModel.updateEpisodeDateToWatch(WATCH_DATE_DONE, (int) listAdapter.getItem(position).getId());
        } else {
            viewModel.updateEpisodeStatus(NOT_WATCHED, (int) listAdapter.getItem(position).getId());
            viewModel.updateEpisodeDateToWatch(WATCH_DATE_NULL, (int) listAdapter.getItem(position).getId());
        }
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbar,
                NavHostFragment.findNavController(this),
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void observeData() {
        viewModel.getEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodesLists -> listAdapter.submitList(myAnimeEpisodesLists));
    }

    private void obtainArguments() {
        animeId = CalendarEpisodesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }
}
