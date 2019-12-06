package com.example.animecalendar.ui.assignment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.example.animecalendar.R;
import com.example.animecalendar.databinding.FragmentAssignmentDatesBinding;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignmentFragment extends Fragment {

    private FragmentAssignmentDatesBinding b;
    private AssignmentFragmentViewModel viewModel;
    private AssignmentFragmentViewAdapter listAdapter;
    private int animeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.ASSIGNMENT))
                .get(AssignmentFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_assignment_dates, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        obtainArguments();
        setupRecyclerView();
        setupToolbar();
        observeData();
        restoreDataFromDeviceRotation();
        pickDateRange();
    }

    private void pickDateRange() {
        b.innerInclude.cosmoCalendar.setSelectionManager(new RangeSelectionManager(() -> {
            viewModel.setCalendarLiveData(b.innerInclude.cosmoCalendar.getSelectedDates());
            viewModel.setAssignableDates(b.innerInclude.cosmoCalendar.getSelectedDates());
            if (ValidationUtils.assignmentController(viewModel.getAssignableDates().size(), listAdapter.getItemCount())) {
                Toast.makeText(requireContext(), String.valueOf(viewModel.getAssignableDates().size()), Toast.LENGTH_SHORT).show();
                viewModel.setSchedule(viewModel.assignDateToEpisodes(viewModel.getAssignableDates(), getAllEpisodes()));
                b.innerInclude.textviewlol.setText(viewModel.getSchedule());
                //toggleSelectedDays();
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //viewModel.setAssignableDates(b.innerInclude.cosmoCalendar.getSelectedDates());
    }

    private void obtainArguments() {
        animeId = AssignmentFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }

    private void setupRecyclerView() {
        listAdapter = new AssignmentFragmentViewAdapter();
        b.innerInclude.listEpisodesAssign.setAdapter(listAdapter);
        b.innerInclude.listEpisodesAssign.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupToolbar() {
        b.toolbarAssign.inflateMenu(R.menu.assigment_menu);
        b.toolbarAssign.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.clearAssignment) {
                clearAssignment();
                return true;
            }
            return false;
        });
        NavigationUI.setupWithNavController(b.toolbarAssign,
                NavHostFragment.findNavController(this),
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void observeData() {
        viewModel.getEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodeListWithAnimeTitles -> {
            setToolbarTitle(myAnimeEpisodeListWithAnimeTitles.get(0).getAnimeTitle());
            listAdapter.submitList(myAnimeEpisodeListWithAnimeTitles);
        });
        viewModel.getCalendarLiveData().observe(getViewLifecycleOwner(), calendars -> {
            if (calendars.size() > 0) {
                enableFab();
            } else {
                b.fab.hide();
            }
        });
    }

    private void setToolbarTitle(String title) {
        b.toolbarAssign.setTitle(title);
    }

    private void enableFab() {
        b.fab.show();
        b.fab.setOnClickListener(view -> commitUpdate());
    }

    private List<MyAnimeEpisodeListWithAnimeTitle> getAllEpisodes() {
        List<MyAnimeEpisodeListWithAnimeTitle> mList = new ArrayList<>();
        for (int i = 0; i < listAdapter.getItemCount(); i++) {
            mList.add(listAdapter.getItem(i));
        }
        return mList;
    }

    private void clearAssignment() {
        viewModel.setAssignableDates(b.innerInclude.cosmoCalendar.getSelectedDates());
        viewModel.setCalendarLiveData(new ArrayList<>());
        if (viewModel.getAssignableDates().size() > 0) {
            b.innerInclude.cosmoCalendar.clearSelections();
            viewModel.getAssignableDates().clear();
            b.innerInclude.textviewlol.setText(
                    getResources().getString(R.string.assign_resume, listAdapter.getItemCount()));
        }
    }

    private void restoreDataFromDeviceRotation() {
        new Handler().postDelayed(() -> b.innerInclude.textviewlol.setText(viewModel.getSchedule() == null ?
                getResources().getString(R.string.assign_resume, listAdapter.getItemCount()) : viewModel.getSchedule()), 100);
        if (viewModel.getAssignableDates() != null) {
            if (viewModel.getAssignableDates().size() > 0) {
                toggleSelectedDays();
            }
        }
    }

    private void toggleSelectedDays() {
        b.innerInclude.cosmoCalendar.getSelectionManager().toggleDay(new Day(viewModel.getAssignableDates().get(0)));
        b.innerInclude.cosmoCalendar.getSelectionManager().toggleDay(new Day(viewModel.getAssignableDates().get(
                viewModel.getAssignableDates().size() - 1
        )));
    }

    private void commitUpdate() {
        if (viewModel.getAssignableDates() != null) {
            if (ValidationUtils.assignmentController(viewModel.getAssignableDates().size(), listAdapter.getItemCount())) {
                viewModel.commitEpisodesDateAssignation(viewModel.getAssignableDates(), getAllEpisodes());
                viewModel.updateStatus((int) listAdapter.getItem(0).getAnimeId());
                Toast.makeText(requireContext(), getResources().getString(R.string.assign_success,
                        listAdapter.getItem(0).getAnimeTitle()), Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();

            } else {
                Toast.makeText(requireContext(), getResources().getString(R.string.warning_pick_assign), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), getResources().getString(R.string.warning_assign), Toast.LENGTH_SHORT).show();
        }

    }
}
