package com.example.animecalendar.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentCalendarAnimeItemBinding;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.model.CalendarAnimeEpisodesRecycled;

import java.util.Locale;

public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodesRecycled, BaseViewHolder<CalendarAnimeEpisodesRecycled>> {

    private static final int ANIME_TYPE = 0;
    private static final int EPISODE_TYPE = 1;
    private int id_temp = -1;
    //TODO
    private static DiffUtil.ItemCallback<CalendarAnimeEpisodesRecycled> diffUtilItemCallback = new DiffUtil.ItemCallback<CalendarAnimeEpisodesRecycled>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarAnimeEpisodesRecycled oldItem, @NonNull CalendarAnimeEpisodesRecycled newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarAnimeEpisodesRecycled oldItem, @NonNull CalendarAnimeEpisodesRecycled newItem) {
            return false;
        }
    };

    public CalendarFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<CalendarAnimeEpisodesRecycled> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ANIME_TYPE:
                return new AnimeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.fragment_calendar_anime_item, parent, false));
            case EPISODE_TYPE:
                return new EpisodeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.fragment_calendar_episode_item, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<CalendarAnimeEpisodesRecycled> holder, int position) {
        holder.bind(getItem(position));
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getViewtype() == ANIME_TYPE) {
            return ANIME_TYPE;
        } else {
            return EPISODE_TYPE;
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<CalendarAnimeEpisodesRecycled> holder) {
        super.onViewRecycled(holder);
    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodesRecycled> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodesRecycled type) {
            b.lblAnime.setText(type.getAnimeTitle());
        }
    }

    class EpisodeViewHolder extends BaseViewHolder<CalendarAnimeEpisodesRecycled> {

        private FragmentCalendarEpisodeItemBinding b;

        EpisodeViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodesRecycled type) {
            String format = "%d - %s";
            String formatTwo = "%dmins";
            b.lblEpisode.setText(String.format(Locale.US, format, type.getNumber(), type.getTitle()));
            b.lblDate.setText(type.getWatchToDate());
            b.lblLength.setText(String.format(Locale.US, formatTwo, type.getLength()));
        }
    }
}
