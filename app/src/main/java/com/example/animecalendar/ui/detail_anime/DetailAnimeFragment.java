package com.example.animecalendar.ui.detail_anime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.animecalendar.base.recycler.BaseListAdapter;
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
        restoreViews();
    }

    private void obtainArguments() {
        animeId = DetailAnimeFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAnimeId();
    }

    private void setupViews() {
        setupToolbar();
        setupRecyclerView();
        setupButtons();
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
//        b.animeDetailed.listEpisodes.setHasFixedSize(true);
        b.animeDetailed.listEpisodes.setItemAnimator(new DefaultItemAnimator());
        b.animeDetailed.listEpisodes.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.animeDetailed.listEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.animeDetailed.listEpisodes.setAdapter(listAdapter);

    }

    private void setupButtons() {
        b.animeDetailed.btnEpisodes.setOnClickListener(v -> {
            if (!viewModel.isCollapseEpisodes()) {
                b.animeDetailed.listEpisodes.setVisibility(View.VISIBLE);
            } else {
                b.animeDetailed.listEpisodes.setVisibility(View.GONE);
            }
            viewModel.setCollapseEpisodes(!viewModel.isCollapseEpisodes());
        });
        b.animeDetailed.btnSynopsis.setOnClickListener(v -> {
            if (!viewModel.isCollapseSynopsis()) {
                b.animeDetailed.lblSynopsis.setVisibility(View.VISIBLE);
            } else {
                b.animeDetailed.lblSynopsis.setVisibility(View.GONE);
            }
            viewModel.setCollapseSynopsis(!viewModel.isCollapseSynopsis());
        });
        b.animeDetailed.btnCharacters.setOnClickListener(view -> { // todo cambiar
            navController.navigate(DetailAnimeFragmentDirections
            .actionDetailAnimeFragmentToCharactersFragment(animeId));
        });
    }

    private void observeData() {
        viewModel.getAnime(animeId).observe(getViewLifecycleOwner(),
                myAnime -> {
                    b.collapsingToolbar.setTitle(myAnime.getCanonicalTitle());
                    PicassoUtils.loadPicasso(requireContext(), myAnime.getMediumPosterImage(), b.header);
                    b.animeDetailed.lblSynopsis.setText(myAnime.getSynopsis());
                });
        viewModel.getAnimeEpisodes(animeId).observe(getViewLifecycleOwner(), myAnimeEpisodesLists -> listAdapter.submitList(myAnimeEpisodesLists));
    }

    private void restoreViews() {
        b.animeDetailed.lblSynopsis.setVisibility(!viewModel.isCollapseSynopsis() ? View.GONE: View.VISIBLE);
        b.animeDetailed.listEpisodes.setVisibility(!viewModel.isCollapseEpisodes() ? View.GONE: View.VISIBLE);
    }
}
