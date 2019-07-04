package com.example.animecalendar.ui.series;

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
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.databinding.FragmentMyanimesBinding;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.List;

public class MyAnimeSeriesFragment extends Fragment {

    private FragmentMyanimesBinding b;
    private NavController navController;
    private MyAnimeSeriesFragmentViewModel viewModel;
    private MyAnimeSeriesFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.SERIES))
                .get(MyAnimeSeriesFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_myanimes, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolbar();
        setupRecyclerView();
        observeData();
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(b.collapsingToolbar,
                b.appbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void setupRecyclerView() {
        listAdapter = new MyAnimeSeriesFragmentViewAdapter();
        //TODO ADAPTER LISTENERS
        listAdapter.setOnItemClickListener((view, position) -> navController.navigate(MyAnimeSeriesFragmentDirections
                .actionMyAnimeSeriesFragmentToDetailAnimeFragment()
                .setAnimeId(listAdapter.getItem(position).getId())));
        b.listAnimes.setHasFixedSize(true);
        b.listAnimes.setItemAnimator(new DefaultItemAnimator());
        b.listAnimes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listAnimes.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listAnimes.setAdapter(listAdapter);
    }

    private void observeData(){
        viewModel.getAnimesToExpose().observe(getViewLifecycleOwner(), animesForSeries -> listAdapter.submitList(animesForSeries));
    }
}
