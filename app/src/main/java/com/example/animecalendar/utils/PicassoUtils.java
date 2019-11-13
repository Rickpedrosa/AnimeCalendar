package com.example.animecalendar.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.animecalendar.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoUtils {

    public static void loadPicasso(Context context, String url, ImageView img) {
        Picasso.with(context)
                .load(url)
                .into(img);
    }

    public static void loadPicassoWithError(Context context,
                                            String url,
                                            ImageView img) {
        Picasso.with(context)
                .load(url)
                .error(R.drawable.ic_person_w_24dp)
                .into(img);
    }

    public static void loadPicassoWithErrorAndCallback(Context context,
                                                       String url,
                                                       ImageView img,
                                                       Callback callback) {
        Picasso.with(context)
                .load(url)
                .error(R.drawable.ic_person_w_24dp)
                .into(img, callback);
    }

    private PicassoUtils() {
    }
}
