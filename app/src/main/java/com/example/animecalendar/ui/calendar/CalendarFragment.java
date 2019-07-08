package com.example.animecalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.model.CalendarAnimeEpisodesRecycled;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements OnSelectDateListener {

    //    private CalendarView calendarView;
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        calendarView = ViewCompat.requireViewById(requireView(), R.id.calendarView);
//        DatePickerBuilder builder = new DatePickerBuilder(requireContext(), this)
//                .setPickerType(CalendarView.ONE_DAY_PICKER);
//
//        DatePicker datePicker = builder.build();
//        datePicker.show();
        navController = NavHostFragment.findNavController(this);
        setupViews(requireView());
        observeData();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.calendar_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.hideEpisodes) {
//            listAdapter.hideAllEpisodes();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setupViews(View view) {
        setupRecyclerView(view);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = ViewCompat.requireViewById(view, R.id.toolbar_calendarFragment);
        toolbar.inflateMenu(R.menu.calendar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.hideEpisodes) {
                listAdapter.hideAllEpisodes();
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

    private void setupRecyclerView(View view) {
        listEpisodes = ViewCompat.requireViewById(view, R.id.listEpisodesCalendar);
        listAdapter = new CalendarFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view1, position) -> {
            if (listAdapter.getItem(position).getViewtype() == CalendarFragmentViewAdapter.ANIME_TYPE) {
                if (listAdapter.getItem(position).getCollapse() == CalendarAnimeEpisodesRecycled.EXPAND_TITLE) {
                    listAdapter.hideItems(position);
                    listAdapter.getItem(position).setCollapse(CalendarAnimeEpisodesRecycled.COLLAPSE_TITLE);
                } else {
                    listAdapter.showItems(position);
                    listAdapter.getItem(position).setCollapse(CalendarAnimeEpisodesRecycled.EXPAND_TITLE);
                }
            } else if (listAdapter.getItem(position).getViewtype() == CalendarFragmentViewAdapter.EPISODE_TYPE) {
                if (listAdapter.getItem(position).getWasWatched() == 0) {
                    viewModel.updateEpisodeStatus(1, listAdapter.getItem(position).getEpisodeId());
                } else {
                    viewModel.updateEpisodeStatus(0, listAdapter.getItem(position).getEpisodeId());
                }
            }
        });
        listEpisodes.setItemAnimator(new DefaultItemAnimator());
        listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        listEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        listEpisodes.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getAnimeWithEpisodes().observe(getViewLifecycleOwner(), calendarAnimeEpisodes ->
                listAdapter.submitList(viewModel.listFormatted(calendarAnimeEpisodes)));
    }

    @Override
    public void onSelect(List<Calendar> calendar) {

    }
}
