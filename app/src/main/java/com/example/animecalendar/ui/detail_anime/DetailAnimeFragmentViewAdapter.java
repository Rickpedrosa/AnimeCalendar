package com.example.animecalendar.ui.detail_anime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.EpisodeItemBinding;
import com.example.animecalendar.model.MyAnimeEpisodesList;

public class DetailAnimeFragmentViewAdapter extends BaseListAdapter<MyAnimeEpisodesList, BaseViewHolder<MyAnimeEpisodesList>> {

    private static DiffUtil.ItemCallback<MyAnimeEpisodesList> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeEpisodesList>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeEpisodesList oldItem, @NonNull MyAnimeEpisodesList newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeEpisodesList oldItem, @NonNull MyAnimeEpisodesList newItem) {
            return false;
        }
    };

    DetailAnimeFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeEpisodesList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeEpisodesList> holder, int position) {
        holder.bind(getItem(position));
    }

    class EpisodeViewHolder extends BaseViewHolder<MyAnimeEpisodesList> {

        private EpisodeItemBinding b;

        EpisodeViewHolder(EpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(MyAnimeEpisodesList type) {
            b.lblEpNumber.setText(String.valueOf(type.getNumber()));
            b.lblTitle.setText(type.getCanonicalTitle());
        }
    }
}
