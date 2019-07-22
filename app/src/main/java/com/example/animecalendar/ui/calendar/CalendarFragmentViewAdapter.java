package com.example.animecalendar.ui.calendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentCalendarAnimeItemBinding;
import com.example.animecalendar.model.CalendarAnime;
import com.example.animecalendar.utils.PicassoUtils;

public class CalendarFragmentViewAdapter extends BaseListAdapter<CalendarAnime, BaseViewHolder<CalendarAnime>> {

    private static DiffUtil.ItemCallback<CalendarAnime> diffUtilItemCallback = new DiffUtil.ItemCallback<CalendarAnime>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarAnime oldItem, @NonNull CalendarAnime newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarAnime oldItem, @NonNull CalendarAnime newItem) {
            return oldItem.getEpCount() == newItem.getEpCount() && oldItem.getEpWatchedCount() == newItem.getEpWatchedCount();
        }
    };

    CalendarFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<CalendarAnime> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_calendar_anime_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<CalendarAnime> holder, int position) {
        holder.bind(getItem(position));
    }

    class AnimeViewHolder extends BaseViewHolder<CalendarAnime> {

        private FragmentCalendarAnimeItemBinding b;

        AnimeViewHolder(FragmentCalendarAnimeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(CalendarAnime type) {
            setDefaultStyle(type);
        }

        private void setDefaultStyle(CalendarAnime type) {
            b.lblAnime.setText(type.getTitle());
            b.lblEpsCounter.setText(b.lblEpsCounter.getResources().getString(R.string.epsCounter,
                    type.getEpWatchedCount(), type.getEpCount()));
           PicassoUtils.loadPicassoWithError(b.imgAnime.getContext(), type.getTinyPosterImage(), b.imgAnime);
        }
    }
}
