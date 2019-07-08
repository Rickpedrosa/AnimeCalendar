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
import com.example.animecalendar.model.CalendarAnimeEpisodesRecycled;

import java.util.Locale;

public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodesRecycled, BaseViewHolder<CalendarAnimeEpisodesRecycled>> {

    static final int ANIME_TYPE = 0;
    private static final int EPISODE_TYPE = 1;
    static final int HIDDEN_ITEM_TYPE = 2;
    private OnAnimeArrowClick onAnimeArrowClick;
    private boolean showOrHide;
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

    CalendarFragmentViewAdapter(OnAnimeArrowClick onAnimeArrowClick) {
        super(diffUtilItemCallback);
        this.onAnimeArrowClick = onAnimeArrowClick;
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
            case HIDDEN_ITEM_TYPE:
                return new DummyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_calendar_dummy_item, parent, false));
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
        } else if (getItem(position).getViewtype() == EPISODE_TYPE) {
            return EPISODE_TYPE;
        } else {
            return HIDDEN_ITEM_TYPE;
        }
    }

    void showItems(int position) {
        //position parameter del elemento anime click ej-> 0
        //showOrHide-> false(2), true(1)default
        int itemCount = 0;

        for (int i = (position + 1); i < getItemCount(); i++) {
            if (getItem(i).getViewtype() == ANIME_TYPE) {
                itemCount = i;
                break;
            }
            if (i == getItemCount() - 1) {
                itemCount = getItemCount();
            }
            getItem(i).setViewtype(EPISODE_TYPE);
        }
        notifyItemRangeChanged(position, itemCount);
    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodesRecycled> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodesRecycled type) {
            b.imgOptions.setOnClickListener(v -> onAnimeArrowClick.hideItems(getAdapterPosition()));
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

    class DummyViewHolder extends BaseViewHolder<CalendarAnimeEpisodesRecycled> {

        DummyViewHolder(View itemView) {
            super(itemView, getOnItemClickListener(), getOnItemLongClickListener());
        }

        @Override
        public void bind(CalendarAnimeEpisodesRecycled type) {
        }
    }
}
