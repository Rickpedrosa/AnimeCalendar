package com.example.animecalendar.ui.detail_item;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.animecalendar.R;
import com.example.animecalendar.databinding.FragmentDetailGenericBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.PicassoUtils;

import java.util.Objects;

public class DetailItemFragment extends Fragment {

    private FragmentDetailGenericBinding b;
    private long characterId = -1;
    private long episodeId = -1;
    private DetailItemFragmentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtainArguments();
        viewModel = ViewModelProviders.of(
                this,
                VMProvider.viewModelFragmentFactory(
                        this,
                        VMProvider.FRAGMENTS.DETAIL_ITEM))
                .get(DetailItemFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_detail_generic,
                container,
                false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        b.lblDescriptionItem.setMovementMethod(new ScrollingMovementMethod());
        observeData();
    }

    private void obtainArguments() {
        DetailItemFragmentArgs args = DetailItemFragmentArgs
                .fromBundle(Objects.requireNonNull(getArguments()));
        characterId = args.getCharacterId();
        episodeId = args.getEpisodeId();
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(
                b.toolbarCharacter,
                NavHostFragment.findNavController(this),
                AppbarConfigProvider.getAppBarConfiguration()
        );
    }

    private void observeData() {
        if (characterId != -1) {
            viewModel.getAnimeCharacter(characterId).observe(getViewLifecycleOwner(), it ->
                    setupViews(
                            it.getCanonicalName(),
                            it.getImage(),
                            it.getDescription()
                    ));
        } else if (episodeId != -1) {
            viewModel.getAnimeEpisode(episodeId).observe(getViewLifecycleOwner(), it ->
                    setupViews(
                            it.getCanonicalTitle(),
                            it.getThumbnail(),
                            it.getSynopsis()
                    ));
        }
    }

    private void setupViews(String title, String img, String body) {
        b.toolbarCharacter.setTitle(title);
        PicassoUtils.loadPicassoWithErrorXY(
                getContext(),
                img,
                b.imgItem
        );
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            b.lblDescriptionItem.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY));
        } else {
            b.lblDescriptionItem.setText(Html.fromHtml(body));
        }
    }
}
