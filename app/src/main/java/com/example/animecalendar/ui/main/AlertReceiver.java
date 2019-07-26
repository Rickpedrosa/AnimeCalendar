package com.example.animecalendar.ui.main;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.preference.PreferenceManager;

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

        PreferenceManager preferenceManager = new PreferenceManager(context);
        boolean pref = preferenceManager.getSharedPreferences()
                .getBoolean(context.getResources().getString(R.string.notification_key), true);
        int timePref = preferenceManager.getSharedPreferences()
                .getInt(context.getResources().getString(R.string.time_notification_key), 90);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ONE)
                .setSmallIcon(R.drawable.ic_build_white_menu_24dp)
                .setContentTitle(mTitle)
                .setContentText(mSubtitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mContent))
                .setContentIntent(getNavigationPendingIntent(context))
                .build();

        notificationManager.notify(App.CHANNEL_ONE_INT, notification);
    }

    private void obtainArguments(Intent intent) {
        mTitle = intent.getStringExtra(TITLE_EXTRA);
        mSubtitle = intent.getStringExtra(CONTENT_EXTRA);
        mContent = intent.getStringExtra(BIG_CONTENT_EXTRA);
    }

    private PendingIntent getNavigationPendingIntent(Context context) { //TODO CAMBIAR DESTINO
        return new NavDeepLinkBuilder(context).setComponentName(MainActivity.class)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.calendarFragment)
                .createPendingIntent();
    }
}
