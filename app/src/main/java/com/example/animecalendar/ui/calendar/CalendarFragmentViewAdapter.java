package com.example.animecalendar.ui.calendar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.model.CalendarAnimeEpisodes;

public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodes, BaseViewHolder<CalendarAnimeEpisodes>> {

    public CalendarFragmentViewAdapter(DiffUtil.ItemCallback<CalendarAnimeEpisodes> diffUtilItemCallback) {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<CalendarAnimeEpisodes> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<CalendarAnimeEpisodes> holder, int position) {

    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes>{

        public AnimeViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {

        }
    }

    class EpisodeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes>{

        public EpisodeViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {

        }
    }
}
