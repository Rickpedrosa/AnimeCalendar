package com.example.animecalendar.ui.calendar_episodes;

import android.content.DialogInterface;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animecalendar.R;
import com.example.animecalendar.base.dialogs.YesNoDialogFragment;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeBinding;
import com.example.animecalendar.model.AnimeEpDateStatusPOJO;
import com.example.animecalendar.model.AnimeEpisodeDateUpdatePOJO;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.ValidationUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.animecalendar.data.local.LocalRepository.NOT_WATCHED;
import static com.example.animecalendar.data.local.LocalRepository.WATCHED;

public class CalendarEpisodesFragment extends Fragment implements YesNoDialogFragment.Listener {

    private FragmentCalendarEpisodeBinding b;
    private long animeId;
    private CalendarEpisodesFragmentViewAdapter listAdapter;
    private CalendarEpisodesFragmentViewModel viewModel;
    private LinearLayoutManager linearLayoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity(),
                VMProvider.viewModelFragmentFactory(requireActivity(), VMProvider.FRAGMENTS.CALENDAR_EPISODES))
                .get(CalendarEpisodesFragmentViewModel.class);
        linearLayoutManager = new LinearLayoutManager(requireContext());
        obtainArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_calendar_episode,
                container,
                false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolbar();
        setupFab();
        setupRecyclerView();
        observeData();
        viewModel.reorderCapsConfirmationPreference().observe(getViewLifecycleOwner(), aBoolean ->
                listAdapter.setOnItemClickListener((view, position) -> {
                    try {
                        CalendarEpisodesFragment.this.updateEpisode(position, aBoolean);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = Objects.requireNonNull(b.listEpisodes.getLayoutManager()).onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRemoving()) {
            mListState = null;
            mBundleRecyclerViewState = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(() -> {
                mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                Objects.requireNonNull(b.listEpisodes.getLayoutManager()).onRestoreInstanceState(mListState);
            }, 50);
        }
        b.listEpisodes.setLayoutManager(linearLayoutManager);
    }

    private void setupFab() {
        b.fab.setOnClickListener(v -> linearLayoutManager
                .scrollToPositionWithOffset(getPositionToScroll(), 10));
    }

    private void setupRecyclerView() {
        listAdapter = new CalendarEpisodesFragmentViewAdapter();
        b.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listEpisodes.setLayoutManager(linearLayoutManager);
        b.listEpisodes.setAdapter(listAdapter);

    }

    private int getPositionToScroll() {
        for (int i = listAdapter.getItemCount() - 1; i >= 0; i--) {
            if (listAdapter.getItem(i).getWasWatched() == WATCHED) {
                return i;
            }
        }
        return 0;
    }

    private void updateEpisode(int position, boolean preference) throws ParseException {
        MyAnimeEpisodesList episode = listAdapter.getItem(position);

        if (episode.getWasWatched() == WATCHED) {
            viewModel.updateEpisodeStatusOnly(
                    LocalRepository.NOT_WATCHED,
                    (int) episode.getId());
        } else if (episode.getWasWatched() == NOT_WATCHED) {
            if (episode.getWatchToDate().equals(LocalRepository.WATCH_DATE_DONE)) {
                viewModel.updateEpisodeStatusOnly(
                        LocalRepository.WATCHED,
                        (int) episode.getId());
            } else {
                if (ValidationUtils.isEqualDate(episode.getWatchToDate())) {
                    innerUpdateEpisode(position);
                } else if (ValidationUtils.isMinorDate(episode.getWatchToDate())) {
                    innerUpdateEpisode(position);
                } else {
                    viewModel.setEpisodeToReorderPosition(position);
                    if (preference) {
                        YesNoDialogFragment.newInstance(
                                getResources().getString(R.string.calendar_episodes_dialog_title),
                                getResources().getString(R.string.calendar_episodes_dialog_message),
                                getResources().getString(R.string.confirm_okay),
                                getResources().getString(R.string.confirm_cancel),
                                CalendarEpisodesFragment.this,
                                6).show(requireFragmentManager(), "POGGU");
                    } else {
                        viewModel.reorderCaps(getNonWatchedEpisodes(position));
                    }
                }
            }
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

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void observeData() {
        viewModel.getEpisodes(animeId).observe(getViewLifecycleOwner(), it -> {
            listAdapter.submitList(it);
            if (areAllWatched(it.size())) {
                viewModel.updateAnimeStatus(animeId);
                navController.popBackStack();
            }
        });
    }

    private boolean areAllWatched(int sourceSize) {
        boolean updateAndPop = false;
        int counter = 0;
        for (int i = 0; i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getWasWatched() == WATCHED) counter++;
        }
        if (counter == sourceSize - 1) updateAndPop = true;
        return updateAndPop;
    }

    private List<AnimeEpisodeDateUpdatePOJO> getNonWatchedEpisodes(int position) throws ParseException {
        List<AnimeEpisodeDateUpdatePOJO> mList = new ArrayList<>();
        for (int i = position; i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getWasWatched() == NOT_WATCHED &&
                    !ValidationUtils.isEqualDate(listAdapter.getItem(i).getWatchToDate()) &&
                    !ValidationUtils.isMinorDate(listAdapter.getItem(i).getWatchToDate())
            ) {
                mList.add(new AnimeEpisodeDateUpdatePOJO(
                        listAdapter.getItem(i).getId(),
                        listAdapter.getItem(i).getWatchToDate()
                ));
            }
        }
        return mList;
    }

    private void obtainArguments() {
        animeId = CalendarEpisodesFragmentArgs.fromBundle(Objects
                .requireNonNull(getArguments()))
                .getAnimeId();
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog) {
        try {
            viewModel.reorderCaps(getNonWatchedEpisodes(viewModel.getEpisodeToReorderPosition()));
        } catch (ParseException e) {
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog) {
        dialog.dismiss();
    }
}
