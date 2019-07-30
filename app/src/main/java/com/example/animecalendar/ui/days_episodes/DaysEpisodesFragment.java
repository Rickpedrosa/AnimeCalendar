package com.example.animecalendar.ui.days_episodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeBinding;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.util.Objects;

import static com.example.animecalendar.data.local.LocalRepository.NOT_WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCHED;

public class DaysEpisodesFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private DaysEpisodesFragmentViewModel viewModel;
    private FragmentCalendarEpisodeBinding b;
    private String date;
    private boolean isToday = false;
    private DaysEpisodesFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.DAYS_EPISODE))
                .get(DaysEpisodesFragmentViewModel.class);
        linearLayoutManager = new LinearLayoutManager(requireContext());
        obtainArguments();
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
        setupRecyclerView();
        setupToolbar();
        setupFab();
        observeData();
    }

    private void setupRecyclerView() {
        listAdapter = new DaysEpisodesFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> updateEpisode(position));
        b.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listEpisodes.setLayoutManager(linearLayoutManager);
        b.listEpisodes.setAdapter(listAdapter);
    }

    private void updateEpisode(int position) {
        if (isToday) {
            innerUpdateEpisode(position);
        } else {
            Toast.makeText(requireContext(), getResources().getString(R.string.not_today), Toast.LENGTH_SHORT).show();
        }
    }

    private void innerUpdateEpisode(int position) {
        if (listAdapter.getItem(position).getWasWatched() == NOT_WATCHED) {
            viewModel.updateStatusAndDateEpisode(
                    new AnimeEpDateStatusPOJO(
                            listAdapter.getItem(position).getId(),
                            LocalRepository.WATCH_DATE_DONE,
                            LocalRepository.WATCHED
                    )
            );
        }
    }

    private void observeData() {
        viewModel.getEpisodes(date).observe(getViewLifecycleOwner(), list -> {
            listAdapter.submitList(list);
            if (list.size() == 0){
                NavHostFragment.findNavController(this).popBackStack(R.id.calendarFragment, false);
            }
        });
    }

    private void setupFab() {
        b.fab.setOnClickListener(v -> linearLayoutManager.scrollToPositionWithOffset(getPositionToScroll(), 10));
    }

    private int getPositionToScroll() {
        for (int i = listAdapter.getItemCount() - 1; i >= 0; i--) {
            if (listAdapter.getItem(i).getWasWatched() == WATCHED) {
                return i;
            }
        }
        return 0;
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbar,
                NavHostFragment.findNavController(this),
                AppbarConfigProvider.getAppBarConfiguration()
        );
        b.toolbar.setTitle(getResources().getString(R.string.daysfragment_title, date));
    }

    private void obtainArguments() {
        //fetxa en string, si coincide con hoy se pueden actualizar los caps, si no no
        date = DaysEpisodesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getDate();
        isToday = CustomTimeUtils.isToday(date);
    }

}
