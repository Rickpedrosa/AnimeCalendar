package com.example.animecalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.animecalendar.R;
import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.animecalendar.data.local.LocalRepository.WATCH_DATE_NULL;
import static com.example.animecalendar.model.CalendarAnimeEpisodesConstants.COLLAPSE_TITLE;
import static com.example.animecalendar.model.CalendarAnimeEpisodesConstants.EXPAND_TITLE;
import static com.example.animecalendar.ui.calendar.CalendarFragmentViewAdapter.ANIME_TYPE;
import static com.example.animecalendar.ui.calendar.CalendarFragmentViewAdapter.EPISODE_TYPE;
import static com.example.animecalendar.ui.calendar.CalendarFragmentViewAdapter.HIDDEN_ITEM_TYPE;

public class CalendarFragment extends Fragment implements OnSelectDateListener {

    private CalendarView calendarView;
    private RecyclerView listEpisodes;
    private NavController navController;
    private CalendarFragmentViewAdapter listAdapter;
    private CalendarFragmentViewModel viewModel;

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
        return inflater.inflate(R.layout.outer_fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        DatePicker datePicker = builder.build();
//        datePicker.show();
        navController = NavHostFragment.findNavController(this);
        setupViews(requireView());
        observeData();
    }

    private void setupViews(View view) {
        setupRecyclerView(view);
        setupToolbar(view);
        setupCalendarView(view);
    }

    private void setupCalendarView(View view) {
        calendarView = ViewCompat.requireViewById(view, R.id.calendarView);
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = ViewCompat.requireViewById(view, R.id.toolbar_calendarFragment);
        toolbar.inflateMenu(R.menu.calendar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideEpisodes) {
                hideAllItems();
                return true;
            }
            return false;
        });
        NavigationUI.setupWithNavController(
                toolbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void setupRecyclerView(View layoutView) {
        listEpisodes = ViewCompat.requireViewById(layoutView, R.id.listEpisodesCalendar);
        listAdapter = new CalendarFragmentViewAdapter(CalendarFragment.this::changeEpisodeStatus);
        listAdapter.setOnItemClickListener((view, position) -> onEpisodeClickLogic(position));
        listAdapter.setOnItemLongClickListener((view, position) -> {
            if (listAdapter.getItem(position).getViewType() == ANIME_TYPE) {
                if (!isTheAnimeMarkedToWatch(position)) {
                    DatePickerBuilder builder = new DatePickerBuilder(requireContext(), calendar
                            -> viewModel.assignDateToEpisodes(calendar,
                            getEpisodesOfAnime(listAdapter.getItem(position).getAnimeId())))
                            .setPickerType(CalendarView.RANGE_PICKER);

                    DatePicker datePicker = builder
                            .build();
                    datePicker.show();
                } else {
                    //TODO EL ANIME YA TIENE FETXAS PUESTAS, PREGUNTAR SI QUIERE REINICIARLAS O ALGO
                    Snackbar.make(calendarView, "All episodes are dated already", Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
            return false;
        });
        listEpisodes.setItemAnimator(new DefaultItemAnimator());
        listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        listEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        listEpisodes.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getAnimeWithEpisodes().observe(getViewLifecycleOwner(), calendarAnimeEpisodes ->
                listAdapter.submitList(calendarAnimeEpisodes));
    }

    @Override
    public void onSelect(List<Calendar> calendar) {

    }

    private void showItems(int position) {
        for (int i = (position + 1); i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getViewType() == ANIME_TYPE) {
                break;
            }
            viewModel.updateEpisodeViewType(EPISODE_TYPE, listAdapter.getItem(i).getEpisodeId());
        }
        viewModel.updateEpisodeCollapse(EXPAND_TITLE, listAdapter.getItem(position).getEpisodeId());
    }

    private void hideItems(int position) {
        for (int i = (position + 1); i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getViewType() == ANIME_TYPE) {
                break;
            }
            viewModel.updateEpisodeViewType(HIDDEN_ITEM_TYPE, listAdapter.getItem(i).getEpisodeId());
        }
        viewModel.updateEpisodeCollapse(COLLAPSE_TITLE, listAdapter.getItem(position).getEpisodeId());
    }

    private void hideAllItems() {
        for (int i = 0; i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getViewType() == EPISODE_TYPE) {
                viewModel.updateEpisodeViewType(HIDDEN_ITEM_TYPE, listAdapter.getItem(i).getEpisodeId());
            } else if (listAdapter.getItem(i).getViewType() == ANIME_TYPE) {
                viewModel.updateEpisodeCollapse(COLLAPSE_TITLE, listAdapter.getItem(i).getEpisodeId());
            }
        }
    }

    private void onEpisodeClickLogic(int position) {
        if (listAdapter.getItem(position).getViewType() == ANIME_TYPE) {
            if (listAdapter.getItem(position).getCollapse() == EXPAND_TITLE) {
                hideItems(position);
            } else {
                showItems(position);
            }
        }
        if (listAdapter.getItem(position).getWasWatched() == 0 && listAdapter.getItem(position).getViewType() != ANIME_TYPE) {
            viewModel.updateEpisodeStatus(1, listAdapter.getItem(position).getEpisodeId());
        } else if (listAdapter.getItem(position).getViewType() != ANIME_TYPE) {
            viewModel.updateEpisodeStatus(0, listAdapter.getItem(position).getEpisodeId());
        }
    }

    private void changeEpisodeStatus(int position) {
        if (listAdapter.getItem(position).getWasWatched() == 0) {
            viewModel.updateEpisodeStatus(1, listAdapter.getItem(position).getEpisodeId());
        } else {
            viewModel.updateEpisodeStatus(0, listAdapter.getItem(position).getEpisodeId());
        }
    }

    private boolean isTheAnimeMarkedToWatch(int position) {
        boolean flag = false;
        int animeId = listAdapter.getItem(position).getAnimeId();
        for (int i = (position); i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getAnimeId() != animeId) {
                break;
            }
            if (flag)
                break;
            flag = !listAdapter.getItem(i).getWatchToDate().equals(WATCH_DATE_NULL);
        }
        return flag;
    }

    private List<CalendarAnimeEpisodes> getEpisodesOfAnime(int animeId) {
        List<CalendarAnimeEpisodes> mEpisodes = new ArrayList<>();
        for (int i = 0; i < listAdapter.getItemCount(); i++) {
            if (listAdapter.getItem(i).getAnimeId() == animeId && listAdapter.getItem(i).getWasWatched() == 0) {
                mEpisodes.add(listAdapter.getItem(i));
            }
        }
        return mEpisodes;
    }
}
