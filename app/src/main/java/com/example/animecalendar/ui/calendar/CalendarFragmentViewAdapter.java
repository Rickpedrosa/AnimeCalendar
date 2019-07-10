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

@SuppressWarnings("WeakerAccess")
public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodes, BaseViewHolder<CalendarAnimeEpisodes>> {

    public static final int ANIME_TYPE = 0;
    public static final int EPISODE_TYPE = 1;
    public static final int HIDDEN_ITEM_TYPE = 2;
    //TODO
    private static DiffUtil.ItemCallback<CalendarAnimeEpisodes> diffUtilItemCallback = new DiffUtil.ItemCallback<CalendarAnimeEpisodes>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarAnimeEpisodes oldItem, @NonNull CalendarAnimeEpisodes newItem) {
            return oldItem.getAnimeId() == newItem.getAnimeId() && oldItem.getEpisodeId() == newItem.getEpisodeId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarAnimeEpisodes oldItem, @NonNull CalendarAnimeEpisodes newItem) {
            return oldItem.getViewType() == newItem.getViewType() &&
                    oldItem.getWasWatched() == newItem.getWasWatched() &&
                    oldItem.getWatchToDate().equals(newItem.getWatchToDate()) &&
                    oldItem.getCollapse() == newItem.getCollapse();
        }
    };

    CalendarFragmentViewAdapter() {
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
            case HIDDEN_ITEM_TYPE:
                return new DummyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_calendar_dummy_item, parent, false));
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
        if (getItem(position).getViewType() == ANIME_TYPE) {
            return ANIME_TYPE;
        } else if (getItem(position).getViewType() == EPISODE_TYPE) {
            return EPISODE_TYPE;
        } else {
            return HIDDEN_ITEM_TYPE;
        }
    }

    void hideAllEpisodes() {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).getViewType() == EPISODE_TYPE) {
                getItem(i).setViewType(HIDDEN_ITEM_TYPE);
            } else if (getItem(i).getViewType() == ANIME_TYPE) {
                getItem(i).setCollapse(CalendarAnimeEpisodesRecycled.COLLAPSE_TITLE);
            }
        }
        notifyDataSetChanged();
    }

    void showItems(int position) {
        int itemCount = 0;
        for (int i = (position + 1); i < getItemCount(); i++) {
            if (getItem(i).getViewType() == ANIME_TYPE) {
                itemCount = i;
                break;
            }
            if (i == getItemCount() - 1) {
                itemCount = getItemCount();
            }
            getItem(i).setViewType(EPISODE_TYPE);
        }
        notifyItemRangeChanged(position, itemCount);
    }

    void hideItems(int position) {
        int itemCount = 0;
        for (int i = (position + 1); i < getItemCount(); i++) {
            if (getItem(i).getViewType() == ANIME_TYPE) {
                itemCount = i;
                break;
            }
            if (i == getItemCount() - 1) {
                itemCount = getItemCount();
            }
            getItem(i).setViewType(HIDDEN_ITEM_TYPE);
        }
        notifyItemRangeChanged(position, itemCount);
    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {
            setDefaultStyle();
            b.imgOptions.setImageResource(getItem(getAdapterPosition()).getCollapse() == CalendarAnimeEpisodesRecycled.EXPAND_TITLE
                    ? R.drawable.ic_keyboard_arrow_up_w_24dp : R.drawable.ic_keyboard_arrow_down_w_24dp);
            b.imgCheck.setImageResource(getItem(getAdapterPosition()).getWasWatched() == 1 ?
                    R.drawable.ic_check_circle_green_24dp : R.drawable.ic_check_circle_black_24dp);
            b.lblAnime.setText(type.getAnimeTitle());
            b.lblEpTitle.setText(type.getEpisodeTitle());
        }

        private void setDefaultStyle() {
            b.imgCheck.setImageResource(R.drawable.ic_check_circle_black_24dp);
            b.imgOptions.setImageResource(R.drawable.ic_keyboard_arrow_down_w_24dp);
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
            setDefaultStyle();
            b.lblEpisode.setText(String.format(Locale.US, "%d - %s", type.getNumber(), type.getEpisodeTitle()));
            b.lblDate.setText(type.getWatchToDate());
            b.lblLength.setText(String.format(Locale.US, "%dmins", type.getLength()));
            b.imgCheck.setImageResource(getItem(getAdapterPosition()).getWasWatched() == 1 ?
                    R.drawable.ic_check_circle_green_24dp : R.drawable.ic_check_circle_black_24dp);
        }

        private void setDefaultStyle() {
            b.imgCheck.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }
    }

    class DummyViewHolder extends BaseViewHolder<CalendarAnimeEpisodes> {

        DummyViewHolder(View itemView) {
            super(itemView, getOnItemClickListener(), getOnItemLongClickListener());
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {
        }
    }
}
