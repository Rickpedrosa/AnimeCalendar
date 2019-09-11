package com.example.animecalendar.ui.series;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animecalendar.R;
import com.example.animecalendar.base.SimpleDividerItemDecoration;
import com.example.animecalendar.base.dialogs.DirectSelectionDialogFragmentMaterial;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentMyanimesBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class MyAnimeSeriesFragment extends Fragment implements DirectSelectionDialogFragmentMaterial.Listener {

    private FragmentMyanimesBinding b;
    private NavController navController;
    private MyAnimeSeriesFragmentViewModel viewModel;
    private MyAnimeSeriesFragmentViewAdapter listAdapter;
    static final String ALL_CATEGORY = "All";
    private SearchView searchView;
    private MenuItem mnuSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(requireActivity(),
                VMProvider.viewModelFragmentFactory(requireActivity(), VMProvider.FRAGMENTS.SERIES))
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

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Restore searching state (in this order).
        String query = viewModel.getSearchQuery();
        if (viewModel.isSearchViewExpanded()) {
            // If done directly, menu item disappears after collapsing.
            b.listAnimes.post(() -> {
                mnuSearch.expandActionView();
                if (!TextUtils.isEmpty(query)) {
                    searchView.setQuery(query, false);
                }
            });
        }
    }

    private void setupToolbar() {
        b.appbar.inflateMenu(R.menu.myanimes_menu);
        setupSearchView();

        b.appbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.mnuAll: //0
                    viewModel.setCategoryToLiveData(ALL_CATEGORY);
                    return true;
                case R.id.mnuFollowing: //1
                    viewModel.setCategoryToLiveData(LocalRepository.STATUS_FOLLOWING);
                    return true;
                case R.id.mnuFinished: //2
                    viewModel.setCategoryToLiveData(LocalRepository.STATUS_FINISHED);
                    return true;
                case R.id.mnuCurrent: //3
                    viewModel.setCategoryToLiveData(LocalRepository.STATUS_CURRENT);
                    return true;
                case R.id.mnuCompleted: //4
                    viewModel.setCategoryToLiveData(LocalRepository.STATUS_COMPLETED);
                    return true;
                case R.id.mnuSettings:
                    navController.navigate(R.id.settingsFragment);
                    return true;
                default:
                    return false;
            }
        });
        NavigationUI.setupWithNavController(
                b.appbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void setupSearchView() {
        mnuSearch = b.appbar.getMenu().findItem(R.id.mnuSearch);
        searchView = (SearchView) mnuSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        mnuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                viewModel.setSearchViewExpanded(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                viewModel.setSearchViewExpanded(false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter adapter when text is changed
                // listAdapter.getFilter().filter(query);
                viewModel.setSearchQuery(query);
                return false;
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        listAdapter = new MyAnimeSeriesFragmentViewAdapter(position -> {
            viewModel.setItemPosition(position);
            DirectSelectionDialogFragmentMaterial.newInstance(
                    listAdapter.getItem(position).getStatus(),
                    this,
                    4)
                    .show(requireFragmentManager(), "XD");
        });
        listAdapter.setOnItemClickListener((view, position) -> navController.navigate(MyAnimeSeriesFragmentDirections
                .actionMyAnimeSeriesFragmentToDetailAnimeFragment()
                .setAnimeId(listAdapter.getItem(position).getId())));
        b.listAnimes.setItemAnimator(new DefaultItemAnimator());
        b.listAnimes.addItemDecoration(new SimpleDividerItemDecoration(Color.parseColor("#FFA823"), 1));
        b.listAnimes.setLayoutManager(manager);
        b.listAnimes.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getTypePreference().observe(getViewLifecycleOwner(), s -> viewModel.setCategoryToLiveData(s));

        viewModel.getAnimesToExposeByCategory().observe(getViewLifecycleOwner(), animesForSeries -> {
            b.lblNoAnimes.setVisibility(animesForSeries.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(animesForSeries);
        });

        viewModel.getUpdateTrigger().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean.hasBeenHandled()) {
                if (aBoolean.getContentIfNotHandled()) {
                    Toast.makeText(requireContext(),
                            getResources().getString(R.string.series_warning_update),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireContext(),
                            getResources().getString(R.string.series_warning_update_okay),
                            Toast.LENGTH_LONG).show();
                    viewModel.retrieveEpisodes(listAdapter.getItem(viewModel.getItemPosition()).getId());
                }
            }
        });
    }


    private void deleteAnime(BottomSheetDialogFragment dialog) {
        viewModel.deleteAnime(listAdapter.getItem(viewModel.getItemPosition()).getId());
        dialog.dismiss();
    }

    private void updateStatus() {
        viewModel.updateStatus(LocalRepository.STATUS_FINISHED, listAdapter.getItem(viewModel.getItemPosition()).getId());
        //TODO CAMBIAR ESTADO DE EPISODIOS POR VER A NULL, ES DECIR, A -
        Snackbar.make(b.listAnimes,
                getResources().getString(R.string.update_anime_status,
                        listAdapter.getItem(viewModel.getItemPosition()).getTitle(), LocalRepository.STATUS_FINISHED),
                Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean onNavItemSelected(BottomSheetDialogFragment dialog, MenuItem item) {
        switch (listAdapter.getItem(viewModel.getItemPosition()).getStatus()) {
            case LocalRepository.STATUS_COMPLETED:
                if (item.getItemId() == R.id.dialog_completed_delete) {
                    deleteAnime(dialog);
                    return true;
                }
                break;
            case LocalRepository.STATUS_CURRENT:
                switch (item.getItemId()) {
                    case R.id.dialog_current_follow:
                        Toast.makeText(requireContext(), "Current animes cannot be followed", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        return true;
                    case R.id.dialog_current_delete:
                        deleteAnime(dialog);
                        return true;
                    case R.id.dialog_current_update:
                        viewModel.checkIfCanBeUpdated(listAdapter.getItem(viewModel.getItemPosition()));
                        dialog.dismiss();
                        return true;
                }
                break;
            case LocalRepository.STATUS_FINISHED:
                switch (item.getItemId()) {
                    case R.id.dialog_finished_follow:
                        navController.navigate(MyAnimeSeriesFragmentDirections
                                .actionMyAnimeSeriesFragmentToAssignmentFragment(
                                        listAdapter.getItem(viewModel.getItemPosition()).getId()));
                        dialog.dismiss();
                        return true;
                    case R.id.dialog_finished_delete:
                        deleteAnime(dialog);
                        return true;
                }
                break;
            case LocalRepository.STATUS_FOLLOWING:
                switch (item.getItemId()) {
                    case R.id.dialog_following_unfollow:
                        updateStatus();
                        dialog.dismiss();
                        return true;
                    case R.id.dialog_following_delete:
                        deleteAnime(dialog);
                        return true;
                }
                break;
        }
        dialog.dismiss();
        return false;
    }
}
