package com.example.animecalendar.ui.characters;

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

import com.example.animecalendar.R;
import com.example.animecalendar.base.CharacterPagerAdapter;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.databinding.FragmentCharactersBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;

import java.util.List;
import java.util.Objects;

public class CharactersFragment extends Fragment {

    private FragmentCharactersBinding b;
    private CharactersFragmentViewModel viewModel;
    private NavController navController;
    private int animeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.CHARACTERS))
                .get(CharactersFragmentViewModel.class);
        obtainArguments();
        viewModel.testAnimeCharacterIDSApiCall(animeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupToolbar();
        setupViewPager();
    }

    private void obtainArguments() {
        animeId = CharactersFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbarCharacter,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void setupViewPager() {
        viewModel.getCharacters().observe(getViewLifecycleOwner(), animeCharacterDetails ->
                b.viewPagerCharacter.setAdapter(new CharacterPagerAdapter(
                        requireContext(),
                        animeCharacterDetails
                )));
    }
}
