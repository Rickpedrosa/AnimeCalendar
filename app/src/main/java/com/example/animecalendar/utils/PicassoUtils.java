package com.example.animecalendar.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoUtils {

    public static void loadPicasso(Context context, String url, ImageView img) {
        Picasso.with(context)
                .load(url)
                .into(img);
    }

    private PicassoUtils() {
    }
}
