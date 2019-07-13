package com.example.animecalendar.ui.calendar_episodes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.EpisodeItemBinding;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.ui.detail_anime.DetailAnimeFragmentViewAdapter;
import com.example.animecalendar.utils.PicassoUtils;

public class CalendarEpisodesFragmentViewAdapter extends BaseListAdapter<MyAnimeEpisodesList, BaseViewHolder<MyAnimeEpisodesList>> {

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

    CalendarEpisodesFragmentViewAdapter() {
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
            b.lblEpisode.setText(b.lblEpisode.getResources().getString(R.string.episode, type.getNumber(), type.getCanonicalTitle()));
            b.lblDate.setText(type.getWatchToDate());
            b.imgCheck.setImageResource(type.getWasWatched() == 0 ?
                    R.drawable.ic_check_circle_black_24dp : R.drawable.ic_check_circle_green_24dp);
            PicassoUtils.loadPicasso(b.empresaImg.getContext(), type.getThumbnail(), b.empresaImg);
        }
    }
}
