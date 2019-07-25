package com.example.animecalendar.ui.main;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.animecalendar.App;
import com.example.animecalendar.R;

import static com.example.animecalendar.ui.main.MainActivity.BIG_CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.TITLE_EXTRA;

public class AlertReceiver extends BroadcastReceiver {

    private String mTitle;
    private String mSubtitle;
    private String mContent;

    @Override
    public void onReceive(Context context, Intent intent) {
        obtainArguments(intent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ONE)
                .setSmallIcon(R.drawable.ic_build_white_menu_24dp)
                .setContentTitle(mTitle)
                .setContentText(mSubtitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mContent))
                .setContentIntent(new NavDeepLinkBuilder(context).setComponentName(MainActivity.class)
                        .setGraph(R.navigation.main_navigation)
                        .setDestination(R.id.calendarFragment)
                        .createPendingIntent())
                .build();
        notificationManager.notify(1, notification);
    }

    private void obtainArguments(Intent intent) {
        mTitle = intent.getStringExtra(TITLE_EXTRA);
        mSubtitle = intent.getStringExtra(CONTENT_EXTRA);
        mContent = intent.getStringExtra(BIG_CONTENT_EXTRA);
    }
}
