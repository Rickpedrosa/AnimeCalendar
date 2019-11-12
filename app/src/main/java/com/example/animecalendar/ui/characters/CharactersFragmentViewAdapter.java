package com.example.animecalendar.ui.characters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.utils.PicassoUtils;

public class CharactersFragmentViewAdapter extends BaseListAdapter<AnimeCharacterDetail, BaseViewHolder<AnimeCharacterDetail>> {

    private static DiffUtil.ItemCallback<AnimeCharacterDetail> diffUtilItemCallback = new DiffUtil.ItemCallback<AnimeCharacterDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull AnimeCharacterDetail oldItem, @NonNull AnimeCharacterDetail newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AnimeCharacterDetail oldItem, @NonNull AnimeCharacterDetail newItem) {
            return false;
        }
    };

    CharactersFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<AnimeCharacterDetail> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_calendar_episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<AnimeCharacterDetail> holder, int position) {
        holder.bind(getItem(position));
    }

    class CharacterViewHolder extends BaseViewHolder<AnimeCharacterDetail> {

        private FragmentCalendarEpisodeItemBinding b;

        CharacterViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(AnimeCharacterDetail type) {
            String image = type.getData().getAttributes().getImage() == null ?
                    null : type.getData().getAttributes().getImage().getOriginal();
            PicassoUtils.loadPicassoWithError(b.empresaImg.getContext(),
                    image,
                    b.empresaImg);
            b.lblEpisode.setText(type.getData().getAttributes().getCanonicalName());
            b.lblDate.setVisibility(View.GONE);
            b.imgCheck.setVisibility(View.GONE);
        }
    }
}
