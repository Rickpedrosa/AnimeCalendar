package com.example.animecalendar.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.example.animecalendar.R;
import com.example.animecalendar.base.recycler.BaseListAdapter;
import com.example.animecalendar.base.recycler.BaseViewHolder;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.databinding.FragmentSearchItemBinding;
import com.example.animecalendar.utils.PicassoUtils;

public class SearchFragmentViewAdapter extends BaseListAdapter<MyAnime, BaseViewHolder<MyAnime>> {

    private static DiffUtil.ItemCallback<MyAnime> diffUtilItemCallback = new DiffUtil.ItemCallback<MyAnime>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyAnime oldItem, @NonNull MyAnime newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyAnime oldItem, @NonNull MyAnime newItem) {
            return false;
        }
    };

    SearchFragmentViewAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public BaseViewHolder<MyAnime> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.fragment_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<MyAnime> holder, int position) {
        holder.bind(getItem(position));
    }

    class AnimeViewHolder extends BaseViewHolder<MyAnime> {

        private FragmentSearchItemBinding b;

        AnimeViewHolder(FragmentSearchItemBinding itemView) {
            super(itemView.getRoot(), getOnItemClickListener(), getOnItemLongClickListener());
            b = itemView;
        }

        @Override
        public void bind(MyAnime type) {
            PicassoUtils.loadPicasso(b.imgCoverTiny.getContext(), type.getCoverImage(), b.imgCoverTiny);
            b.lblEpisodeCount.setText(b.lblCanonicalTitle.getContext()
                    .getResources().getString(R.string.search_episodes_label,
                            type.getEpisodeCount()));
            b.lblCanonicalTitle.setText(type.getCanonicalTitle());
        }
    }
}
