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
import com.example.animecalendar.data.local.entity.MyAnimeCharacter;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.utils.PicassoUtils;

public class CharactersFragmentViewAdapter extends BaseListAdapter<MyAnimeCharacter, BaseViewHolder<MyAnimeCharacter>> {

    private static DiffUtil.ItemCallback<MyAnimeCharacter> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeCharacter>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeCharacter oldItem, @NonNull MyAnimeCharacter newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeCharacter oldItem, @NonNull MyAnimeCharacter newItem) {
            return false;
        }
    };

    CharactersFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeCharacter> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_calendar_episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeCharacter> holder, int position) {
        holder.bind(getItem(position));
    }

    class CharacterViewHolder extends BaseViewHolder<MyAnimeCharacter> {

        private FragmentCalendarEpisodeItemBinding b;

        CharacterViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(MyAnimeCharacter type) {
            PicassoUtils.loadPicassoWithError(b.empresaImg.getContext(),
                    type.getImage(),
                    b.empresaImg);
            b.lblEpisode.setText(type.getCanonicalName());
            b.lblDate.setVisibility(View.GONE);
            b.imgCheck.setVisibility(View.GONE);
        }
    }
}
