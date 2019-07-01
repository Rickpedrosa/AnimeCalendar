package com.example.animecalendar.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.animecalendar.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class SearchFragment extends Fragment {

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = ViewCompat.requireViewById(requireView(), R.id.toolbar_detailFragment);
        CollapsingToolbarLayout collapsingToolbarLayout = ViewCompat.requireViewById(requireView(), R.id.collapsingToolbar);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.myAnimeSeriesFragment,
                        R.id.calendarFragment,
                        R.id.searchFragment)
                        .build();
        NavigationUI.setupWithNavController(collapsingToolbarLayout, toolbar, navController, appBarConfiguration);
    }
}
