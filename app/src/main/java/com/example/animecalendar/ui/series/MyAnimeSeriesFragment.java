package com.example.animecalendar.ui.series;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
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
import com.example.animecalendar.base.Event;
import com.example.animecalendar.base.dialogs.DirectSelectionDialogFragment;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentMyanimesBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MyAnimeSeriesFragment extends Fragment implements DirectSelectionDialogFragment.Listener {

    private FragmentMyanimesBinding b;
    private NavController navController;
    private MyAnimeSeriesFragmentViewModel viewModel;
    private MyAnimeSeriesFragmentViewAdapter listAdapter;
    static final String ALL_CATEGORY = "All";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(requireActivity(),
                VMProvider.viewModelFragmentFactory(requireActivity(), VMProvider.FRAGMENTS.SERIES))
                .get(MyAnimeSeriesFragmentViewModel.class);
        if (savedInstanceState == null)
            viewModel.setCategoryToLiveData(ALL_CATEGORY);
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
        b.appbar.inflateMenu(R.menu.myanimes_menu);
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
                default:
                    return false;
            }
        });
        NavigationUI.setupWithNavController(b.collapsingToolbar,
                b.appbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void setupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
//        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
//        Drawable mDivider = ContextCompat.getDrawable(requireContext(), R.drawable.my_series_divider);
//        divider.setDrawable(Objects.requireNonNull(mDivider));
        listAdapter = new MyAnimeSeriesFragmentViewAdapter(position -> {
            DirectSelectionDialogFragment ds = DirectSelectionDialogFragment.newInstance(
                    "",
                    getArrayForDialog(position),
                    MyAnimeSeriesFragment.this,
                    2
            );
            viewModel.setItemPosition(position);
            ds.show(requireFragmentManager(), "POG");
        });
        listAdapter.setOnItemClickListener((view, position) -> navController.navigate(MyAnimeSeriesFragmentDirections
                .actionMyAnimeSeriesFragmentToDetailAnimeFragment()
                .setAnimeId(listAdapter.getItem(position).getId())));
        b.listAnimes.setItemAnimator(new DefaultItemAnimator());
        b.listAnimes.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        b.listAnimes.setLayoutManager(manager);
        b.listAnimes.setAdapter(listAdapter);
    }

    private void observeData() {
        viewModel.getAnimesToExposeByCategory().observe(getViewLifecycleOwner(), animesForSeries -> {
            b.lblNoAnimes.setVisibility(animesForSeries.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(animesForSeries);
        });

        viewModel.getUpdateTrigger().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean.hasBeenHandled()) {
                if (aBoolean.getContentIfNotHandled()) {
                    Toast.makeText(requireContext(), "Son los mismos", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireContext(), "Se puede actualizar", Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.getCategoryTrigger().observe(getViewLifecycleOwner(), s ->
                b.collapsingToolbar.setTitle(getResources().getString(R.string.myseries_fragment_toolbar_title, s)));
    }

    private String[] getArrayForDialog(int position) {
        switch (listAdapter.getItem(position).getStatus()) {
            case LocalRepository.STATUS_COMPLETED:
                return getResources().getStringArray(R.array.dialog_opts_3);
            case LocalRepository.STATUS_CURRENT:
                return getResources().getStringArray(R.array.dialog_opts_4);
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
                } else if (which == 1) {
                    deleteAnime();
                } else {
                    viewModel.checkIfCanBeUpdated(listAdapter.getItem(viewModel.getItemPosition()));
                }
                break;
            case LocalRepository.STATUS_FINISHED:
                if (which == 0) {
                    navController.navigate(MyAnimeSeriesFragmentDirections
                            .actionMyAnimeSeriesFragmentToAssignmentFragment(
                                    listAdapter.getItem(viewModel.getItemPosition()).getId()));
                } else {
                    deleteAnime();
                }
                break;
            case LocalRepository.STATUS_FOLLOWING:
                if (which == 0) {
                    updateStatus(); //do unfollow to the anime
                } else {
                    deleteAnime();
                }
                break;
        }
    }

    private void deleteAnime() {
        viewModel.deleteAnime(listAdapter.getItem(viewModel.getItemPosition()).getId());
    }

    private void updateStatus() {
        viewModel.updateStatus(LocalRepository.STATUS_FINISHED, listAdapter.getItem(viewModel.getItemPosition()).getId());
        Snackbar.make(b.listAnimes,
                getResources().getString(R.string.update_anime_status,
                        listAdapter.getItem(viewModel.getItemPosition()).getTitle(), LocalRepository.STATUS_FINISHED),
                Snackbar.LENGTH_LONG).show();

    }
}
