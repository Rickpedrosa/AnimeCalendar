package com.example.animecalendar.ui.detail_anime;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.palette.graphics.Palette;

import com.example.animecalendar.R;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.databinding.FragmentAnimeDetailBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.PicassoUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
    }

    private void setupButtons() {
        b.animeDetailed.btnEpisodes.setOnClickListener(v ->
                viewModel.setCollapseEpisodes(!viewModel.isCollapseEpisodes()));
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
                    b.animeDetailed.lblSynopsis.setText(myAnime.getSynopsis());
                    PicassoUtils.loadPicassoWithErrorAndCallback(
                            requireContext(),
                            myAnime.getMediumPosterImage(),
                            b.header, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //noinspection CatchMayIgnoreException
                                    try {
                                        setPaletteColoursToUI(myAnime);
                                    } catch (java.lang.IllegalStateException e) { }
                                }

                                @Override
                                public void onError() {

                                }
                            });
                });
        viewModel.getAnimeEpisodes(animeId).observe(getViewLifecycleOwner(), it ->
                listAdapter.submitList(it));
    }

    private void setPaletteColoursToUI(MyAnime myAnime) {
        Picasso
                .with(requireContext())
                .load(myAnime.getMediumPosterImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(palette -> setColorsToButtons(palette));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void setColorsToButtons(Palette palette) {
        int defaultColor = Color.parseColor("#33ff33");
        b.collapsingToolbar.setExpandedTitleColor(Objects.requireNonNull(palette).getVibrantColor(defaultColor));
        b.collapsingToolbar.setCollapsedTitleTextColor(Objects.requireNonNull(palette).getLightVibrantColor(defaultColor));
        b.animeDetailed.btnCharacters.setBackgroundColor(Objects.requireNonNull(palette).getLightVibrantColor(defaultColor));
        b.animeDetailed.btnEpisodes.setBackgroundColor(Objects.requireNonNull(palette).getVibrantColor(defaultColor));
        b.animeDetailed.btnSynopsis.setBackgroundColor(Objects.requireNonNull(palette).getDarkVibrantColor(defaultColor));
        b.animeDetailed.button.setBackgroundColor(Objects.requireNonNull(palette).getLightMutedColor(defaultColor));
        b.animeDetailed.button3.setBackgroundColor(Objects.requireNonNull(palette).getMutedColor(defaultColor));
        b.animeDetailed.button4.setBackgroundColor(Objects.requireNonNull(palette).getDarkMutedColor(defaultColor));
        if (palette.getLightVibrantSwatch() != null) {
            b.animeDetailed.btnCharacters.setTextColor(palette.getLightVibrantSwatch().getTitleTextColor());
        }
        if (palette.getVibrantSwatch() != null) {
            b.animeDetailed.btnEpisodes.setTextColor(palette.getVibrantSwatch().getTitleTextColor());
        }
        if (palette.getDarkVibrantSwatch() != null) {
            b.animeDetailed.btnSynopsis.setTextColor(palette.getDarkVibrantSwatch().getTitleTextColor());
        }
    }

    private void restoreViews() {
        b.animeDetailed.lblSynopsis.setVisibility(!viewModel.isCollapseSynopsis() ? View.GONE : View.VISIBLE);
//        b.animeDetailed.listEpisodes.setVisibility(!viewModel.isCollapseEpisodes() ? View.GONE : View.VISIBLE);
    }
}
