package com.example.animecalendar.ui.episodes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.example.animecalendar.databinding.FragmentCharactersBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.ui.characters.CharactersFragmentDirections;

import java.util.Objects;

public class EpisodesFragment extends Fragment {

    private FragmentCharactersBinding b;
    private EpisodesFragmentViewModel viewModel;
    private NavController navController;
    private int animeId;
    private SearchView searchView;
    private MenuItem mnuSearch;
    private EpisodesFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        obtainArguments();
        VMProvider.FRAGMENTS enumCharacter = VMProvider.FRAGMENTS.EPISODES;
        enumCharacter.setAnimeId(animeId);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, enumCharacter))
                .get(EpisodesFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        b.progressBar.setVisibility(View.INVISIBLE);
        setupToolbar();
        setupAdapter();
        observeData();
    }

    private void obtainArguments() {
        animeId = EpisodesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }

    private void setupAdapter() {
        listAdapter = new EpisodesFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {
            navController.navigate(EpisodesFragmentDirections
                    .actionEpisodesFragmentToDetailItemFragment(
                            listAdapter.getItem(position).getId(),
                            -1));
        });
        b.listCharacters.setItemAnimator(new DefaultItemAnimator());
        b.listCharacters.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listCharacters.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listCharacters.setAdapter(listAdapter);
    }

    private void setupToolbar() {
        b.toolbarCharacter.inflateMenu(R.menu.characters_menu);
        setupSearchView();
        NavigationUI.setupWithNavController(
                b.toolbarCharacter,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void observeData() {
        viewModel.getMyAnimeEpisodes().observe(getViewLifecycleOwner(), myAnimeEpisodesLists ->
                listAdapter.submitList(myAnimeEpisodesLists));
    }

    private void setupSearchView() {
        mnuSearch = b.toolbarCharacter.getMenu().findItem(R.id.mnuSearchCharacters);
        searchView = (SearchView) mnuSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        mnuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                viewModel.setSearchViewExpanded(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                viewModel.setSearchViewExpanded(false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    viewModel.setSearchQuery("");
                }
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Restore searching state (in this order).
        String query = viewModel.getSearchQuery();
        if (viewModel.isSearchViewExpanded()) {
            // If done directly, menu item disappears after collapsing.
            b.listCharacters.post(() -> {
                mnuSearch.expandActionView();
                if (!TextUtils.isEmpty(query)) {
                    searchView.setQuery(query, false);
                }
            });
        }
    }
}
