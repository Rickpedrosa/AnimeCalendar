package com.example.animecalendar.ui.detail_character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.animecalendar.R;
import com.example.animecalendar.databinding.FragmentDetailGenericBinding;

import java.util.Objects;

public class DetailAnimeCharacterFragment extends Fragment {

    private FragmentDetailGenericBinding b;
    private int characterId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    void obtainArguments() {
//        characterId = DetailAnimeCharacterFragmentArgs
//                .fromBundle(Objects.requireNonNull(getArguments())).getCharacterId();
    }
}
