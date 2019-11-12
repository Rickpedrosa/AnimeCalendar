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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animecalendar.R;
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
        viewModel.retrieveCharacters(animeId);
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
        setupProgressBar();
        setupRecyclerView();
    }

    private void setupProgressBar() {
        viewModel.getCharactersLoading().observe(getViewLifecycleOwner(), aBoolean ->
                b.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));
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

    private void setupRecyclerView() {
        viewModel.getCharacters().observe(getViewLifecycleOwner(), this::setupAdapter);
    }

    private void setupAdapter(List<AnimeCharacterDetail> animeCharacterDetails) {
//        b.lblNoCharacters.setVisibility(animeCharacterDetails.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        CharactersFragmentViewAdapter listAdapter = new CharactersFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {
        });
        b.listCharacters.setItemAnimator(new DefaultItemAnimator());
        b.listCharacters.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listCharacters.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listCharacters.setAdapter(listAdapter);
        listAdapter.submitList(animeCharacterDetails);
    }
}
