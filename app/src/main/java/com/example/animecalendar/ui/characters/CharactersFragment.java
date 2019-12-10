package com.example.animecalendar.ui.characters;

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

import java.util.Objects;

public class CharactersFragment extends Fragment {

    private FragmentCharactersBinding b;
    private CharactersFragmentViewModel viewModel;
    private NavController navController;
    private long animeId;
    private SearchView searchView;
    private MenuItem mnuSearch;
    private CharactersFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        obtainArguments();
        VMProvider.FRAGMENTS enumCharacter = VMProvider.FRAGMENTS.CHARACTERS;
        enumCharacter.setAnimeId(animeId);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, enumCharacter))
                .get(CharactersFragmentViewModel.class);
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
        setupAdapter();
        if (savedInstanceState == null) {
            viewModel.getAnimeCharactersWard(animeId).observe(getViewLifecycleOwner(), integer -> {
                if (integer == 0) {
                    viewModel.retrieveCharacters(animeId);
                }
            });
        }
        observeData();
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
        b.toolbarCharacter.inflateMenu(R.menu.characters_menu);
        setupSearchView();
        NavigationUI.setupWithNavController(
                b.toolbarCharacter,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void setupAdapter() {
        listAdapter = new CharactersFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) ->
                navController.navigate(CharactersFragmentDirections
                .actionCharactersFragmentToDetailItemFragment(
                        -1,
                        listAdapter.getItem(position).getId())));
        b.listCharacters.setItemAnimator(new DefaultItemAnimator());
        b.listCharacters.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listCharacters.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listCharacters.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getCharacterAsyncInfo().observe(getViewLifecycleOwner(), stringResource ->
                b.lblInfoCharacters.setText(stringResource.getData()));

        viewModel.getAnimeCharactersV2().observe(getViewLifecycleOwner(), myAnimeCharacters -> {
            if (myAnimeCharacters.size() != 0) {
                listAdapter.submitList(myAnimeCharacters);
            }
        });
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
