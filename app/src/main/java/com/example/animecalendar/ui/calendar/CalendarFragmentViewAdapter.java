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

import java.util.Locale;

public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodes, BaseViewHolder<CalendarAnimeEpisodes>> {

    private static final int ANIME_TYPE = 0;
    private static final int EPISODE_TYPE = 1;
    private int id_temp = -1;
    //TODO
    private static DiffUtil.ItemCallback<CalendarAnimeEpisodes> diffUtilItemCallback = new DiffUtil.ItemCallback<CalendarAnimeEpisodes>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarAnimeEpisodes oldItem, @NonNull CalendarAnimeEpisodes newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarAnimeEpisodes oldItem, @NonNull CalendarAnimeEpisodes newItem) {
            return false;
        }
    };

    public CalendarFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<CalendarAnimeEpisodes> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull BaseViewHolder<CalendarAnimeEpisodes> holder, int position) {
        holder.bind(getItem(position));
    }


    @Override
    public int getItemViewType(int position) {
        int id_marker;
        if (getItem(position).getNumber() == 1 && getItem(position).getAnimeId() != id_temp) {
            id_marker = 0;
        } else {
            id_marker = getItem(position).getAnimeId();
        }

        id_temp = getItem(position).getAnimeId();
        if (id_marker == 0) {
            return ANIME_TYPE;
        } else {
            return EPISODE_TYPE;
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<CalendarAnimeEpisodes> holder) {
        super.onViewRecycled(holder);
    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {
            b.lblAnime.setText(type.getAnimeTitle());
        }
    }

    class EpisodeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes> {

        private FragmentCalendarEpisodeItemBinding b;

        EpisodeViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {
            String format = "%d - %s";
            b.lblEpisode.setText(String.format(Locale.US, format, type.getNumber(), type.getTitle()));
        }
    }
}
