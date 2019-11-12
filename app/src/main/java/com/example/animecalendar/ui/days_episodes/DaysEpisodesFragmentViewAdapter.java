package com.example.animecalendar.ui.days_episodes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.databinding.FragmentCalendarEpisodeItemBinding;
import com.example.animecalendar.model.MyAnimeEpisodesDailyList;
import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.utils.PicassoUtils;

import static com.example.animecalendar.data.local.LocalRepository.NOT_WATCHED;

public class DaysEpisodesFragmentViewAdapter extends BaseListAdapter<MyAnimeEpisodesDailyList, BaseViewHolder<MyAnimeEpisodesDailyList>> {

    private static DiffUtil.ItemCallback<MyAnimeEpisodesDailyList> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnimeEpisodesDailyList>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnimeEpisodesDailyList oldItem, @NonNull MyAnimeEpisodesDailyList newItem) {
            return oldItem.getAnimeId() == newItem.getAnimeId() && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnimeEpisodesDailyList oldItem, @NonNull MyAnimeEpisodesDailyList newItem) {
            return oldItem.getWasWatched() == newItem.getWasWatched();
        }
    };

    DaysEpisodesFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnimeEpisodesDailyList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_calendar_episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnimeEpisodesDailyList> holder, int position) {
        holder.bind(getItem(position));
    }

    class EpisodeViewHolder extends BaseViewHolder<MyAnimeEpisodesDailyList> {

        private FragmentCalendarEpisodeItemBinding b;

        EpisodeViewHolder(FragmentCalendarEpisodeItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(MyAnimeEpisodesDailyList type) {
            setImmutableValues(type);
            setMutableValues(type);
        }

        private void setImmutableValues(MyAnimeEpisodesDailyList type) {
            b.lblEpisode.setText(b.lblEpisode.getResources().getString(R.string.episode, type.getNumber(), type.getCanonicalTitle()));
            b.lblDate.setText(type.getAnimeTitle());
            PicassoUtils.loadPicassoWithError(b.empresaImg.getContext(), type.getThumbnail(), b.empresaImg);
        }

        private void setMutableValues(MyAnimeEpisodesDailyList type) {
            b.lblEpisode.setTextColor(getColor(type.getWasWatched()));
            b.imgCheck.setImageResource(type.getWasWatched() == 0 ?
                    R.drawable.ic_check_circle_black_24dp : R.drawable.ic_check_circle_watched_24dp);
            b.empresaImg.setBorderColor(getColor(type.getWasWatched()));
            b.innerConstraint.setAlpha(type.getWasWatched() == 0 ? 1f : 0.65f);
        }

        private int getColor(int wasWatched) {
            int white = 0xffffffff;
            int golden = 0xFFFFA823;
            return wasWatched == NOT_WATCHED ? white : golden;
        }
    }
}
