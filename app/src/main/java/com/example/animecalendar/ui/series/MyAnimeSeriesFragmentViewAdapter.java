package com.example.animecalendar.ui.series;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentSeriesItemBinding;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.utils.PicassoUtils;

import java.util.Locale;

public class MyAnimeSeriesFragmentViewAdapter extends BaseListAdapter<AnimesForSeries, BaseViewHolder<AnimesForSeries>> {

    private static DiffUtil.ItemCallback<AnimesForSeries> diffUtilItemCallback = new DiffUtil.ItemCallback<AnimesForSeries>() {
        @Override
        public boolean areItemsTheSame(@NonNull AnimesForSeries oldItem, @NonNull AnimesForSeries newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AnimesForSeries oldItem, @NonNull AnimesForSeries newItem) {
            return false;
        }
    };

    MyAnimeSeriesFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<AnimesForSeries> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_series_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<AnimesForSeries> holder, int position) {
        holder.bind(getItem(position));
    }

    class AnimeViewHolder extends BaseViewHolder<AnimesForSeries> {

        private FragmentSeriesItemBinding b;

        AnimeViewHolder(FragmentSeriesItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(AnimesForSeries type) {
            String format = "%d/%d episodes";
            String sformat = "Status: %s";
            b.lblTitle.setText(type.getTitle());
            Log.d("LOGPOG", type.getStatus());
            b.lblStatus.setText(String.format(Locale.US, sformat, type.getStatus()));
            b.lblEpCounts.setText(String.format(Locale.US, format, type.getEpWatchedCount(), type.getEpCount()));
            PicassoUtils.loadPicasso(b.imgPoster.getContext(), type.getPoster(), b.imgPoster);
        }
    }
}
