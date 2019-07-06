package com.example.animecalendar.ui.series;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
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
import com.example.animecalendar.base.dialogs.DirectSelectionDialogFragment;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentMyanimesBinding;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class MyAnimeSeriesFragment extends Fragment implements DirectSelectionDialogFragment.Listener {

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
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        listAdapter = new MyAnimeSeriesFragmentViewAdapter(new OnOptionsClickListener() {
            @Override
            public void showOptions(int position) {
                DirectSelectionDialogFragment ds = DirectSelectionDialogFragment.newInstance(
                        "",
                        getArrayForDialog(position),
                        MyAnimeSeriesFragment.this,
                        2
                );
                viewModel.setItemPosition(position);
                ds.show(requireFragmentManager(), "POG");
            }
        });
        listAdapter.setOnItemClickListener((view, position) -> navController.navigate(MyAnimeSeriesFragmentDirections
                .actionMyAnimeSeriesFragmentToDetailAnimeFragment()
                .setAnimeId(listAdapter.getItem(position).getId())));
        b.listAnimes.setItemAnimator(new DefaultItemAnimator());
        b.listAnimes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listAnimes.setLayoutManager(manager);
        b.listAnimes.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getAnimesToExpose().observe(getViewLifecycleOwner(), animesForSeries -> listAdapter.submitList(animesForSeries));
    }

    private String[] getArrayForDialog(int position) {
        switch (listAdapter.getItem(position).getStatus()) {
            case LocalRepository.STATUS_COMPLETED:
                return getResources().getStringArray(R.array.dialog_opts_3);
            case LocalRepository.STATUS_CURRENT:
                return getResources().getStringArray(R.array.dialog_opts_1);
            case LocalRepository.STATUS_FINISHED:
                return getResources().getStringArray(R.array.dialog_opts_1);
            case LocalRepository.STATUS_FOLLOWING:
                return getResources().getStringArray(R.array.dialog_opts_2);
            default:
                return getResources().getStringArray(R.array.dialog_opts_1);
        }
    }

    @Override
    public void onItemSelected(DialogFragment dialog, int which) {
        switch (listAdapter.getItem(viewModel.getItemPosition()).getStatus()) {
            case LocalRepository.STATUS_COMPLETED:
                if (which == 0) {
                    deleteAnime();
                }
                break;
            case LocalRepository.STATUS_CURRENT:
                if (which == 0) {
                    Toast.makeText(requireContext(), "Current animes cannot be followed", Toast.LENGTH_LONG).show();
                } else {
                    deleteAnime();
                }
                break;
            case LocalRepository.STATUS_FINISHED:
                if (which == 0) {
                    updateStatus(LocalRepository.STATUS_FOLLOWING);
                } else {
                    deleteAnime();
                }
                break;
            case LocalRepository.STATUS_FOLLOWING:
                if (which == 0) {
                    updateStatus(LocalRepository.STATUS_FINISHED); //do unfollow to the anime
                } else {
                    deleteAnime();
                }
                break;
        }
    }

    private void deleteAnime() {
        viewModel.deleteAnime(listAdapter.getItem(viewModel.getItemPosition()).getId());
    }

    private void updateStatus(String status) {
        viewModel.updateStatus(status, listAdapter.getItem(viewModel.getItemPosition()).getId());
        Snackbar.make(b.listAnimes,
                getResources().getString(R.string.update_anime_status,
                        listAdapter.getItem(viewModel.getItemPosition()).getTitle(), status),
                Snackbar.LENGTH_LONG).show();
    }
}
