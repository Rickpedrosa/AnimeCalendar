package com.example.animecalendar.ui.calendar;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.example.animecalendar.R;
import com.example.animecalendar.databinding.OuterFragmentCalendarBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

    private void setupCalendarView() {
//        b.includeCalendarContent.cosmoCalendar.setSelectionManager(new RangeSelectionManager(
//                new OnDaySelectedListener() {
//                    @Override
//                    public void onDaySelected() {
////                    Log.e("PORONGA", "========== setSelectionManager ==========");
////
////                    Log.e("PORONGA", "Selected Dates : " + b.includeCalendarContent.cosmoCalendar.getSelectedDates().size());
//                        if (b.includeCalendarContent.cosmoCalendar.getSelectedDates().size() <= 0) {
//                            return;
//                        }
////                    Log.e("PORONGA", "Selected Days : " + b.includeCalendarContent.cosmoCalendar.getSelectedDays());
////                    Log.e("PORONGA", "Departure : DD MMM YYYY : " +
////                            CustomTimeUtils.getDateFormatted(b.includeCalendarContent.cosmoCalendar.getSelectedDays().get(0).getCalendar().getTime()));
////                    Log.e("PORONGA", "Return : DD MMM YYYY : " +
////                            CustomTimeUtils.getDateFormatted(b.includeCalendarContent.cosmoCalendar.getSelectedDays()
////                                    .get(b.includeCalendarContent.cosmoCalendar.getSelectedDates().size() - 1).getCalendar().getTime()));
//
//                    }
//                }
//        ));

    }

    private void setupViews() {
        setupRecyclerView();
        setupToolbar();
        setupCalendarView();
    }

    private void setupToolbar() {
        b.toolbarCalendarFragment.inflateMenu(R.menu.calendar_menu);
        b.toolbarCalendarFragment.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideEpisodes) {
               b.includeCalendarContent.cosmoCalendar.clearSelections();
                Calendar day = Calendar.getInstance();
                day.set(2019, 7, 23);
                b.includeCalendarContent.cosmoCalendar.getSelectionManager().toggleDay(new Day(Calendar.getInstance()));
                b.includeCalendarContent.cosmoCalendar.getSelectionManager().toggleDay(new Day(day));
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
        viewModel.getAnimeWithEpisodes().observe(getViewLifecycleOwner(), calendarAnimeEpisodes ->
        {
            b.includeCalendarContent.lblNoAnime.setVisibility(calendarAnimeEpisodes.size() == 0 ?
                    View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(calendarAnimeEpisodes);
        });
    }
}
