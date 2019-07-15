package com.example.animecalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.animecalendar.databinding.OuterFragmentCalendarBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

public class CalendarFragment extends Fragment {

    private NavController navController;
    private CalendarFragmentViewAdapter listAdapter;
    private CalendarFragmentViewModel viewModel;
    private OuterFragmentCalendarBinding b;
    private int flag = 2;

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
            if (item.getItemId() == R.id.hideShowCalendar) {
                if (flag % 2 == 0) {
                    b.includeCalendarContent.cosmoCalendar.setVisibility(View.GONE);
                } else {
                    b.includeCalendarContent.cosmoCalendar.setVisibility(View.VISIBLE);
                }
                flag++;
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
