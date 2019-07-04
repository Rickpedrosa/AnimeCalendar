package com.example.animecalendar.ui.series;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.model.MyAnimeList;

public class MyAnimeSeriesFragmentViewAdapter extends BaseListAdapter<MyAnimeList, BaseViewHolder<MyAnimeList>> {

    private static DiffUtil.ItemCallback<MyAnimeList> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeList>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeList oldItem, @NonNull MyAnimeList newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeList oldItem, @NonNull MyAnimeList newItem) {
            return false;
        }
    };

    public MyAnimeSeriesFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeList> holder, int position) {
        holder.bind(getItem(position));
    }

    class AnimeViewHolder extends BaseViewHolder<MyAnimeList>{

        public AnimeViewHolder(View itemView) {
            super(itemView, getOnItemClickListener(), getOnItemLongClickListener());
        }

        @Override
        public void bind(MyAnimeList type) {

        }
    }
}
