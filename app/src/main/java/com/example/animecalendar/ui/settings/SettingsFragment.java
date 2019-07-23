package com.example.animecalendar.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceFragmentCompat;

import com.example.animecalendar.R;
import com.example.animecalendar.providers.AppbarConfigProvider;

public class SettingsFragment extends PreferenceFragmentCompat {

    private NavController navController;

    public SettingsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolBar(requireView());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    private void setupToolBar(View view) {
        Toolbar toolbar = ViewCompat.requireViewById(view, R.id.toolbar_fragmentSettings);
        NavigationUI.setupWithNavController(toolbar, navController, AppbarConfigProvider.getAppBarConfiguration());
    }
}
