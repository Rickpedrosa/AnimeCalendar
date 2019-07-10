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

import static com.example.animecalendar.model.CalendarAnimeEpisodesRecycled.EXPAND_TITLE;

@SuppressWarnings("WeakerAccess")
public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnimeEpisodes, BaseViewHolder<CalendarAnimeEpisodes>> {

    public static final int ANIME_TYPE = 0;
    public static final int EPISODE_TYPE = 1;
    public static final int HIDDEN_ITEM_TYPE = 2;
    private OnAnimeCheckClick onAnimeCheckClick;
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

    CalendarFragmentViewAdapter(OnAnimeCheckClick onAnimeCheckClick) {
        super(diffUtilItemCallback);
        this.onAnimeCheckClick = onAnimeCheckClick;
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

    class AnimeViewHolder extends BaseViewHolder<CalendarAnimeEpisodes> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnimeEpisodes type) {
            setDefaultStyle(type);
            b.imgOptions.setImageResource(getItem(getAdapterPosition()).getCollapse() == EXPAND_TITLE
                    ? R.drawable.ic_keyboard_arrow_up_w_24dp : R.drawable.ic_keyboard_arrow_down_w_24dp);
            b.imgCheck.setVisibility(getItem(getAdapterPosition()).getCollapse() == EXPAND_TITLE
                    ? View.VISIBLE : View.GONE);
            b.lblEpTitle.setVisibility(getItem(getAdapterPosition()).getCollapse() == EXPAND_TITLE
                    ? View.VISIBLE : View.GONE);
            b.imgCheck.setImageResource(getItem(getAdapterPosition()).getWasWatched() == 1 ?
                    R.drawable.ic_check_circle_green_24dp : R.drawable.ic_check_circle_black_24dp);

        }

        private void setDefaultStyle(CalendarAnimeEpisodes type) {
            b.lblAnime.setText(type.getAnimeTitle());
            b.lblEpTitle.setText(String.format(Locale.US, "%d - %s", type.getNumber(), type.getEpisodeTitle()));
            //b.imgCheck.setImageResource(R.drawable.ic_check_circle_black_24dp);
            b.imgOptions.setImageResource(R.drawable.ic_keyboard_arrow_down_w_24dp);
            b.imgCheck.setOnClickListener(v -> onAnimeCheckClick.changeEpisodeStatus(getAdapterPosition()));
            b.lblEpTitle.setOnClickListener(v -> onAnimeCheckClick.changeEpisodeStatus(getAdapterPosition()));
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
            setDefaultStyle(type);
            b.imgCheck.setImageResource(getItem(getAdapterPosition()).getWasWatched() == 1 ?
                    R.drawable.ic_check_circle_green_24dp : R.drawable.ic_check_circle_black_24dp);
        }

        private void setDefaultStyle(CalendarAnimeEpisodes type) {
            b.imgCheck.setImageResource(R.drawable.ic_check_circle_black_24dp);
            b.lblEpisode.setText(String.format(Locale.US, "%d - %s", type.getNumber(), type.getEpisodeTitle()));
            b.lblDate.setText(type.getWatchToDate());
            b.lblLength.setText(String.format(Locale.US, "%dmins", type.getLength()));
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
