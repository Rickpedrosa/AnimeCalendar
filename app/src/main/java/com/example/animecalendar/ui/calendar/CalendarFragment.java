package com.example.animecalendar.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager;
import com.applikeysolutions.cosmocalendar.settings.appearance.ConnectedDayIconPosition;
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.example.animecalendar.R;
import com.example.animecalendar.databinding.OuterFragmentCalendarBinding;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
        calendarVisibility();
    }

    private void setupViews() {
        setupRecyclerView();
        setupToolbar();
        setupCalendarView();
    }

    private void setupCalendarView() {
        b.includeCalendarContent.cosmoCalendar.setConnectedDayIconPosition(ConnectedDayIconPosition.TOP);
        b.includeCalendarContent.cosmoCalendar.setSelectionManager(
                new SingleSelectionManager(() -> navController.navigate(R.id.searchFragment)));
//        viewModel.getDatesFromWatchableEpisodes().observe(getViewLifecycleOwner(), this::setCalendarEventDays);
    }

    private void setCalendarEventDays(List<String> strings) {
        Set<Long> days = new TreeSet<>();
        try {
            for (int i = 0; i < strings.size(); i++) {
                days.add(CustomTimeUtils.dateFromStringToLong(strings.get(i)));
            }
            ConnectedDays connectedDays = new ConnectedDays(days, Color.parseColor("#FFA823"));
            b.includeCalendarContent.cosmoCalendar.addConnectedDays(connectedDays);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
    }

    private void calendarVisibility() {
        if (viewModel.getFlag() % 2 == 0) {
            b.includeCalendarContent.cosmoCalendar.setVisibility(View.GONE);
        } else {
            b.includeCalendarContent.cosmoCalendar.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        b.toolbarCalendarFragment.inflateMenu(R.menu.calendar_menu);
        b.toolbarCalendarFragment.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideShowCalendar) {
                viewModel.setFlag();
                calendarVisibility();
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
        listAdapter.setOnItemClickListener((view, position) -> navController.navigate(CalendarFragmentDirections
                .actionCalendarFragmentToCalendarEpisodesFragment(listAdapter.getItem(position).getId())));
        b.includeCalendarContent.listEpisodesCalendar
                .setItemAnimator(new DefaultItemAnimator());
        b.includeCalendarContent.listEpisodesCalendar
                .addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.includeCalendarContent.listEpisodesCalendar
                .setLayoutManager(new LinearLayoutManager(requireContext()));
        b.includeCalendarContent.listEpisodesCalendar
                .setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getAnimeWithEpisodes().observe(getViewLifecycleOwner(), this::submitDataToAdapter);
    }

    private void submitDataToAdapter(List<CalendarAnime> calendarAnimeEpisodes) {
        listAdapter.submitList(calendarAnimeEpisodes);
        b.includeCalendarContent.lblNoAnime.setVisibility(calendarAnimeEpisodes.size() == 0 ?
                View.VISIBLE : View.INVISIBLE);
    }
}
