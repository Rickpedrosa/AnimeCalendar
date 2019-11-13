package com.example.animecalendar.ui.characters;

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
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
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
        observeData(savedInstanceState);
    }

    private void setupProgressBar() {
        b.progressBar.setVisibility(View.INVISIBLE);
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

    private void setupAdapter(List<MyAnimeCharacter> animeCharacterDetails) {
        CharactersFragmentViewAdapter listAdapter = new CharactersFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {//todo navegar a detalle
        });
        b.listCharacters.setItemAnimator(new DefaultItemAnimator());
        b.listCharacters.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listCharacters.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listCharacters.setAdapter(listAdapter);
        listAdapter.submitList(animeCharacterDetails);
    }

    private void observeData(@Nullable Bundle savedInstanceState) {
        viewModel.getCharacterAsyncInfo().observe(getViewLifecycleOwner(), stringResource ->
                b.lblInfoCharacters.setText(stringResource.getData()));
        if (savedInstanceState == null) {
            viewModel.getAnimeCharacters(animeId).observe(getViewLifecycleOwner(), myAnimeCharacters -> {
                if (myAnimeCharacters.size() == 0) {
                    viewModel.retrieveCharacters(animeId);
                } else {
                    setupAdapter(myAnimeCharacters);
                }
            });
        }
    }
}
