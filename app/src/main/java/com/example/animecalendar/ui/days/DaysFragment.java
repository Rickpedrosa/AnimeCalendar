package com.example.animecalendar.ui.days;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.example.animecalendar.R;
import com.example.animecalendar.base.SimpleDividerItemDecoration;
import com.example.animecalendar.databinding.FragmentDaysBinding;
import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.List;

public class DaysFragment extends Fragment {

    private FragmentDaysBinding b;
    private NavController navController;
    private DaysFragmentViewModel viewModel;
    private DaysFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.DAYS))
                .get(DaysFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_days, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolbar();
        setupRecyclerView();
        viewModel.getDays().observe(getViewLifecycleOwner(), this::submitList);
    }

    private void submitList(List<AnimeEpisodeDates> animeEpisodeDates) {
        listAdapter.submitList(animeEpisodeDates);
        b.lblNoDays.setVisibility(animeEpisodeDates.size() == 0 ?
                View.VISIBLE : View.INVISIBLE);
    }

    private void setupRecyclerView() {
        listAdapter = new DaysFragmentViewAdapter();
        b.listDays.setItemAnimator(new DefaultItemAnimator());
        b.listDays.addItemDecoration(new SimpleDividerItemDecoration(Color.parseColor("#FFA823"), 1));
        b.listDays.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listDays.setAdapter(listAdapter);
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbarDaysFragment,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }
}
