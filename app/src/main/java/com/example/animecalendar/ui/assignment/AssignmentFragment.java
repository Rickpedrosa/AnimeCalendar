package com.example.animecalendar.ui.assignment;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.example.animecalendar.R;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentAssignmentDatesBinding;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AssignmentFragment extends Fragment {

    private FragmentAssignmentDatesBinding b;
    private AssignmentFragmentViewModel viewModel;
    private AssignmentFragmentViewAdapter listAdapter;
    private int animeId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.setAssignableDates(b.innerInclude.cosmoCalendar.getSelectedDates());
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
            switch (item.getItemId()) {
                case R.id.clearAssignment:
                    clearAssignment();
                    return true;
                case R.id.assignment:
                    confirmAssignment(listAdapter.getItemCount());
                    return true;
                case R.id.confirmAssignment:
                    commitUpdate();
                    return true;
                default:
                    return false;
            }
        });
        NavigationUI.setupWithNavController(b.toolbarAssign,
                NavHostFragment.findNavController(this),
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private boolean assignmentController(int days, int caps) {
        return caps >= days && days > 0;
    }

    private void observeData() {
        viewModel.getEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodeListWithAnimeTitles -> {
            setToolbarTitle(myAnimeEpisodeListWithAnimeTitles.get(0).getAnimeTitle());
            listAdapter.submitList(myAnimeEpisodeListWithAnimeTitles);
        });
    }

    private void setToolbarTitle(String title) {
        b.toolbarAssign.setTitle(title);
    }

    private void confirmAssignment(int caps) {
        viewModel.setAssignableDates(b.innerInclude.cosmoCalendar.getSelectedDates());
        if (assignmentController(viewModel.getAssignableDates().size(), caps)) {
            Toast.makeText(requireContext(), String.valueOf(viewModel.getAssignableDates().size()), Toast.LENGTH_SHORT).show();
            viewModel.setSchedule(viewModel.assignDateToEpisodes(viewModel.getAssignableDates(), getAllEpisodes()));
            b.innerInclude.textviewlol.setText(viewModel.getSchedule());
        } else {
            Toast.makeText(requireContext(), "DO NOT", Toast.LENGTH_SHORT).show();
        }
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
        if (viewModel.getAssignableDates().size() > 0) {
            b.innerInclude.cosmoCalendar.clearSelections();
            viewModel.getAssignableDates().clear();
        }
    }

    private void restoreDataFromDeviceRotation() {
        new Handler().postDelayed(() -> b.innerInclude.textviewlol.setText(viewModel.getSchedule() == null ? String.format(Locale.US,
                "%d episodes to assign:", listAdapter.getItemCount()) : viewModel.getSchedule()), 50);
        if (viewModel.getAssignableDates() != null) {
            if (viewModel.getAssignableDates().size() > 0) {
                b.innerInclude.cosmoCalendar.getSelectionManager().toggleDay(new Day(viewModel.getAssignableDates().get(0)));
                b.innerInclude.cosmoCalendar.getSelectionManager().toggleDay(new Day(viewModel.getAssignableDates().get(
                        viewModel.getAssignableDates().size() - 1
                )));
            }
        }
    }

    private void commitUpdate() {
        if (viewModel.getAssignableDates() != null) {
            if (assignmentController(viewModel.getAssignableDates().size(), listAdapter.getItemCount())) {
                viewModel.commitEpisodesDateAssignation(viewModel.getAssignableDates(), getAllEpisodes());
                viewModel.updateStatus(LocalRepository.STATUS_FOLLOWING, (int) listAdapter.getItem(0).getAnimeId());
                Toast.makeText(requireContext(), "DONE!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();

            } else {
                Toast.makeText(requireContext(), "DO NOT", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "You must assign the date range before commit", Toast.LENGTH_SHORT).show();
        }

    }
}
