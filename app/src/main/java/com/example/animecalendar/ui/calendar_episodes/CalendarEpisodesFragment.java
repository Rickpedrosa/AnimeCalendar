package com.example.animecalendar.ui.calendar_episodes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
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
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.animecalendar.data.local.LocalRepository.NOT_WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCH_DATE_DONE;

public class CalendarEpisodesFragment extends Fragment {

    private FragmentCalendarEpisodeBinding b;
    private int animeId;
    private CalendarEpisodesFragmentViewAdapter listAdapter;
    private CalendarEpisodesFragmentViewModel viewModel;
    private LinearLayoutManager linearLayoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.CALENDAR_EPISODES))
                .get(CalendarEpisodesFragmentViewModel.class);
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
        observeData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = Objects.requireNonNull(b.includeCalendarEpisodeContent.listEpisodes.getLayoutManager()).onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(() -> {
                mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                Objects.requireNonNull(b.includeCalendarEpisodeContent.listEpisodes.getLayoutManager()).onRestoreInstanceState(mListState);
            }, 50);
        }
        b.includeCalendarEpisodeContent.listEpisodes.setLayoutManager(linearLayoutManager);
    }

    private void setupRecyclerView() {
        listAdapter = new CalendarEpisodesFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {
            try {
                updateEpisode(position);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        listAdapter.setOnItemLongClickListener((view, position) -> {
            scrollToPositionOnEnteringFragment();
            return true;
        });
        b.includeCalendarEpisodeContent.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.includeCalendarEpisodeContent.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.includeCalendarEpisodeContent.listEpisodes.setLayoutManager(linearLayoutManager);
        b.includeCalendarEpisodeContent.listEpisodes.setAdapter(listAdapter);

    }

    private void scrollToPositionOnEnteringFragment() {
        boolean noItemsWatched = false;
        for (int i = listAdapter.getItemCount() - 1; i >= 0; i--) {
            if (listAdapter.getItem(i).getWasWatched() == WATCHED) {
                linearLayoutManager.scrollToPositionWithOffset(i - 1, 10);
                noItemsWatched = true;
                break;
            }

            if (i == 0) {
                linearLayoutManager.scrollToPositionWithOffset(0, 10);
            }
        }
        if (!noItemsWatched) {
            linearLayoutManager.scrollToPositionWithOffset(0, 10);
        }
    }

    private void updateEpisode(int position) throws ParseException {
        if (CustomTimeUtils.getDateFormatted(Calendar.getInstance().getTime()).equals(listAdapter.getItem(position).getWatchToDate())) {
            if (listAdapter.getItem(position).getWasWatched() == NOT_WATCHED) {
                viewModel.updateEpisodeStatus(WATCHED, (int) listAdapter.getItem(position).getId());
                viewModel.updateEpisodeDateToWatch(WATCH_DATE_DONE, (int) listAdapter.getItem(position).getId());
            }
        } else if (new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(listAdapter.getItem(position).getWatchToDate()).getTime()
                < Calendar.getInstance().getTime().getTime()) {
            if (listAdapter.getItem(position).getWasWatched() == NOT_WATCHED) {
                viewModel.updateEpisodeStatus(WATCHED, (int) listAdapter.getItem(position).getId());
                viewModel.updateEpisodeDateToWatch(WATCH_DATE_DONE, (int) listAdapter.getItem(position).getId());
            }
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
        viewModel.getEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodesLists -> {
            listAdapter.submitList(myAnimeEpisodesLists);
            checkForAnimeStatusUpdate(myAnimeEpisodesLists);
        });
    }

    private void checkForAnimeStatusUpdate(List<MyAnimeEpisodesList> myAnimeEpisodes) {
        int counter = 0;
        for (int i = 0; i < myAnimeEpisodes.size(); i++) {
            if (myAnimeEpisodes.get(i).getWasWatched() == WATCHED) {
                counter++;
            }
        }
        if (counter == (myAnimeEpisodes.size() - 1)) {
            viewModel.updateAnimeStatus(LocalRepository.STATUS_COMPLETED, (int) myAnimeEpisodes.get(0).getAnimeId());
        }
    }

    private void obtainArguments() {
        animeId = CalendarEpisodesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }
}
