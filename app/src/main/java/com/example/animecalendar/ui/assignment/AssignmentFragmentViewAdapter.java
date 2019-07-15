package com.example.animecalendar.ui.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;

public class AssignmentFragmentViewAdapter extends BaseListAdapter<MyAnimeEpisodeListWithAnimeTitle, BaseViewHolder<MyAnimeEpisodeListWithAnimeTitle>> {

    private static DiffUtil.ItemCallback<MyAnimeEpisodeListWithAnimeTitle> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeEpisodeListWithAnimeTitle>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeEpisodeListWithAnimeTitle oldItem, @NonNull MyAnimeEpisodeListWithAnimeTitle newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeEpisodeListWithAnimeTitle oldItem, @NonNull MyAnimeEpisodeListWithAnimeTitle newItem) {
            return false;
        }
    };

    public AssignmentFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeEpisodeListWithAnimeTitle> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DummyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeEpisodeListWithAnimeTitle> holder, int position) {
        holder.bind(getItem(position));
    }

    class DummyViewHolder extends BaseViewHolder<MyAnimeEpisodeListWithAnimeTitle> {

        DummyViewHolder(View itemView) {
            super(itemView, getOnItemClickListener(), getOnItemLongClickListener());
        }

        @Override
        public void bind(MyAnimeEpisodeListWithAnimeTitle type) {
        }
    }
}
