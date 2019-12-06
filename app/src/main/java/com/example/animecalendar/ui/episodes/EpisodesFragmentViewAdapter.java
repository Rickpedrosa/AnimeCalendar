package com.example.animecalendar.ui.episodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.utils.PicassoUtils;
import com.squareup.picasso.Callback;

public class EpisodesFragmentViewAdapter extends BaseListAdapter<MyAnimeEpisodesList, BaseViewHolder<MyAnimeEpisodesList>> {

    private static DiffUtil.ItemCallback<MyAnimeEpisodesList> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeEpisodesList>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeEpisodesList oldItem, @NonNull MyAnimeEpisodesList newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeEpisodesList oldItem, @NonNull MyAnimeEpisodesList newItem) {
            return false;
        }
    };

    public EpisodesFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeEpisodesList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_calendar_episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeEpisodesList> holder, int position) {
        holder.bind(getItem(position));
    }

    class EpisodeViewHolder extends BaseViewHolder<MyAnimeEpisodesList> {

        private FragmentCalendarEpisodeItemBinding b;

        EpisodeViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(MyAnimeEpisodesList type) {
            if (type.getThumbnail() == null) {
                PicassoUtils.loadPicassoWithError(b.empresaImg.getContext(),
                        type.getThumbnail(),
                        b.empresaImg);
            } else {
                b.progressBar2.setVisibility(View.VISIBLE);
                PicassoUtils.loadPicassoWithErrorAndCallback(b.empresaImg.getContext(),
                        type.getThumbnail(),
                        b.empresaImg, new Callback() {
                            @Override
                            public void onSuccess() {
                                b.progressBar2.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                b.progressBar2.setVisibility(View.GONE);
                            }
                        });
            }

            b.lblEpisode.setText(b.lblEpisode.getResources().getString(R.string.episode, type.getNumber(), type.getCanonicalTitle()));
            b.lblDate.setVisibility(View.GONE);
            b.imgCheck.setVisibility(View.GONE);
        }
    }
}
