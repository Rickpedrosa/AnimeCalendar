package com.example.animecalendar.ui.calendar;

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
import com.example.animecalendar.R;
import com.example.animecalendar.databinding.OuterFragmentCalendarBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CalendarFragment extends Fragment implements OnSelectDateListener {

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

//    private void setupCalendarView() {
//        b.includeCalendarContent.calendarView.setEvents(new ArrayList<>(
//                Collections.singletonList(new EventDay(Calendar.getInstance(), R.drawable.ic_today_lol_24dp))
//        ));
//        b.includeCalendarContent.calendarView.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) {
//            }
//        });
//    }

    private void setupViews() {
        setupRecyclerView();
        setupToolbar();
//        setupCalendarView();
    }

    private void setupToolbar() {
        b.toolbarCalendarFragment.inflateMenu(R.menu.calendar_menu);
        b.toolbarCalendarFragment.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideEpisodes) {
                Toast.makeText(requireContext(), "2XTIME", Toast.LENGTH_LONG).show();
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
            b.includeCalendarContent.progressBar.setVisibility(View.INVISIBLE);
            b.includeCalendarContent.lblNoAnime.setVisibility(calendarAnimeEpisodes.size() == 0 ?
                    View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(calendarAnimeEpisodes);
        });
    }

    @Override
    public void onSelect(List<Calendar> calendar) {
    }
}
