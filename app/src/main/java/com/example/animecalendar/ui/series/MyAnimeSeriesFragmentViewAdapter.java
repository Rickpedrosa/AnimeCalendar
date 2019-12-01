package com.example.animecalendar.ui.series;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.databinding.FragmentSeriesItemBinding;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.utils.PicassoUtils;

public class MyAnimeSeriesFragmentViewAdapter extends BaseListAdapter<AnimesForSeries, BaseViewHolder<AnimesForSeries>> {

    private static DiffUtil.ItemCallback<AnimesForSeries> diffUtilItemCallback = new DiffUtil.ItemCallback<AnimesForSeries>() {
        @Override
        public boolean areItemsTheSame(@NonNull AnimesForSeries oldItem, @NonNull AnimesForSeries newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AnimesForSeries oldItem, @NonNull AnimesForSeries newItem) {
            return oldItem.getStatus().equals(newItem.getStatus()) && oldItem.getEpCount() == newItem.getEpCount()
                    && oldItem.getEpWatchedCount() == newItem.getEpWatchedCount() && oldItem.getPoster().equals(newItem.getPoster())
                    && oldItem.getTitle().equals(newItem.getTitle());
        }
    };
    private OnOptionsClickListener onOptionsClickListener;

    MyAnimeSeriesFragmentViewAdapter(OnOptionsClickListener onOptionsClickListener) {
        super(diffUtilItemCallback);
        this.onOptionsClickListener = onOptionsClickListener;
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
            setGeneralInfo(type);
            setStyles(type);
            b.imgItemOpts.setOnClickListener(v -> onOptionsClickListener.showOptions(getAdapterPosition()));
        }

        private void setGeneralInfo(AnimesForSeries type) {
            b.lblTitle.setText(type.getTitle());
            b.lblStatus.setText(b.lblStatus.getResources().getString(R.string.animeStatus,
                    applyStatus(type.getStatus())));
            PicassoUtils.loadPicasso(b.imgPoster.getContext(), type.getPoster(), b.imgPoster);
        }

        private void setStyles(AnimesForSeries type) {
            cleanLabels();
            switch (type.getStatus()) {
                case LocalRepository.STATUS_CURRENT:
                    b.lblTitle.setTextColor(b.lblTitle.getResources().getColor(R.color.colorAccent));
                    b.lblEpCounts.setVisibility(View.INVISIBLE);
                    break;
                case LocalRepository.STATUS_FOLLOWING:
                    b.lblTitle.setTextColor(b.lblTitle.getResources().getColor(R.color.colorBottomItem));
                    break;
                case LocalRepository.STATUS_COMPLETED:
                    b.innerConstraint.setAlpha(0.5f);
                    break;
                default:
                    //FINISHED STATUS
                    b.lblEpCounts.setText(b.lblEpCounts.getResources().getString(R.string.ready_sche));
                    break;
            }
        }

        private void cleanLabels() {
            b.innerConstraint.setAlpha(1f);
            b.lblTitle.setTextColor(b.lblTitle.getResources().getColor(android.R.color.background_light));
            b.lblNextEpisode.setVisibility(View.GONE);
        }

        private String applyStatus(String status) {
            Context context = b.lblStatus.getContext();
            switch (status) {
                case "finished":
                    return context.getResources().getString(R.string.finished_val);
                case "current":
                    return context.getResources().getString(R.string.current_val);
                case "following":
                    return context.getResources().getString(R.string.following_val);
                case "completed":
                    return context.getResources().getString(R.string.completed_val);
                default:
                    return "";
            }
        }
    }
}
