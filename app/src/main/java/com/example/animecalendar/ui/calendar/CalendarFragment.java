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

import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
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
    private int viewAdapterBoolean = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void setupViews(View view) {
        setupRecyclerView(view);
        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = ViewCompat.requireViewById(view, R.id.toolbar_calendarFragment);
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
                listAdapter.setShowOrHide(viewAdapterBoolean % 2 != 0);
                if (viewAdapterBoolean % 2 != 0) {
                    listAdapter.showItems(position);
                } else {
                    listAdapter.hideItems(position);
                }
                viewAdapterBoolean++;
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
