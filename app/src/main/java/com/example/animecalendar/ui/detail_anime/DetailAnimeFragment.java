package com.example.animecalendar.ui.detail_anime;

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
import com.example.animecalendar.databinding.FragmentAnimeDetailBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.PicassoUtils;

import java.util.Objects;

public class DetailAnimeFragment extends Fragment {

    private FragmentAnimeDetailBinding b;
    private NavController navController;
    private DetailAnimeFragmentViewModel viewModel;
    private int animeId;
    private DetailAnimeFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this, VMProvider.FRAGMENTS.DETAIL_ANIME))
                .get(DetailAnimeFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_anime_detail, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        obtainArguments();
        setupViews();
        observeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.disposeObservable();
    }

    private void obtainArguments() {
        animeId = DetailAnimeFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }

    private void setupViews() {
        b.animeDetailed.epProgressBar.setVisibility(View.INVISIBLE);
        setupToolbar();setupRecyclerView();
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.collapsingToolbar,
                b.appbar,
                navController,
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void setupRecyclerView() {
        listAdapter = new DetailAnimeFragmentViewAdapter();
        b.animeDetailed.listEpisodes.setHasFixedSize(true);
        b.animeDetailed.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.animeDetailed.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.animeDetailed.listEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.animeDetailed.listEpisodes.setAdapter(listAdapter);
        b.animeDetailed.listEpisodes.setNestedScrollingEnabled(true);
    }

    private void observeData() {
        viewModel.getAnime(animeId).observe(getViewLifecycleOwner(),
                myAnime -> {
                    b.collapsingToolbar.setTitle(myAnime.getCanonicalTitle());
                    b.animeDetailed.lblSynopsis.setText(myAnime.getSynopsis());
                    PicassoUtils.loadPicasso(requireContext(), myAnime.getMediumPosterImage(), b.header);
                });

        viewModel.getProgressTrigger().observe(getViewLifecycleOwner(), aBoolean ->
                b.animeDetailed.epProgressBar.setVisibility(aBoolean ? View.VISIBLE:View.INVISIBLE));

        viewModel.getAnimeEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodesLists -> {
            if (myAnimeEpisodesLists.size() == 0) {
                viewModel.retrieveRetroEpisodes(animeId);
            } else {
                listAdapter.submitList(myAnimeEpisodesLists);
            }
        });
    }
}
