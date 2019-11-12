package com.example.animecalendar.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.animecalendar.data.remote.pojos.anime_character_detail.AnimeCharacterDetail;
import com.example.animecalendar.utils.PicassoUtils;

import java.util.List;

public class CharacterPagerAdapter extends PagerAdapter {

    private final int mAnimeCharactersAmount;
    private final Context mContext;
    private final List<AnimeCharacterDetail> mAnimeCharacters;

    public CharacterPagerAdapter(Context mContext, List<AnimeCharacterDetail> mAnimeCharacters) {
        this.mAnimeCharactersAmount = mAnimeCharacters.size();
        this.mContext = mContext;
        this.mAnimeCharacters = mAnimeCharacters;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        PicassoUtils.loadPicassoWithError(mContext,
                mAnimeCharacters.get(position).getData().getAttributes().getImage().getOriginal(),
                imageView);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return mAnimeCharactersAmount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }
}
