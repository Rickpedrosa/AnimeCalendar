package com.example.animecalendar.ui.days;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentDaysItemBinding;
import com.example.animecalendar.model.AnimeEpisodeDates;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;

public class DaysFragmentViewAdapter extends BaseListAdapter<AnimeEpisodeDates, BaseViewHolder<AnimeEpisodeDates>> {

    private static DiffUtil.ItemCallback<AnimeEpisodeDates> diffUtilItemCallback = new DiffUtil.ItemCallback<AnimeEpisodeDates>() {
        @Override
        public boolean areItemsTheSame(@NonNull AnimeEpisodeDates oldItem, @NonNull AnimeEpisodeDates newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AnimeEpisodeDates oldItem, @NonNull AnimeEpisodeDates newItem) {
            return oldItem.getDate().equals(newItem.getDate()) && oldItem.getEpsCount() == newItem.getEpsCount();
        }
    };

    DaysFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<AnimeEpisodeDates> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_days_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<AnimeEpisodeDates> holder, int position) {
        holder.bind(getItem(position));
    }

    class DayViewHolder extends BaseViewHolder<AnimeEpisodeDates> {

        private FragmentDaysItemBinding b;

        DayViewHolder(FragmentDaysItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(AnimeEpisodeDates type) {
            b.lblDate.setText(type.getDate());
            b.lblEpCounter.setText(String.valueOf(type.getEpsCount()));
            try {
                b.lblDateString.setText(CustomTimeUtils.getWeekDay(type.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            b.cardDay.setCardBackgroundColor(CustomTimeUtils.isToday(type.getDate()) ?
                    Color.parseColor("#FFA823") : Color.parseColor("#262626"));
        }
    }
}
