package com.example.animecalendar.base;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.animecalendar.App;
import com.example.animecalendar.R;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.ui.main.AlertReceiver;
import com.example.animecalendar.ui.main.MainActivity;

import java.util.List;

import static com.example.animecalendar.ui.main.MainActivity.CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.TITLE_EXTRA;

public class MyJobIntentService extends JobIntentService {
    /* Give the Job a Unique Id */
    private static final int JOB_ID = 1000;
    private String mTitle;
    private String mSubtitle;

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, MyJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
//        obtainArguments(intent);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//        Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ONE)
//                .setSmallIcon(R.drawable.ic_build_white_menu_24dp)
//                .setContentTitle(mTitle)
//                .setContentText(mSubtitle)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                .setContentIntent(getNavigationPendingIntent(getApplicationContext()))
//                .build();
//
//        notificationManager.notify(App.CHANNEL_ONE_INT, notification);
//        AlertReceiver.cancelAlarm(getApplicationContext());
//        stopSelf();
    }

    private void obtainArguments(Intent intent) {
        mTitle = intent.getStringExtra(TITLE_EXTRA);
        mSubtitle = intent.getStringExtra(CONTENT_EXTRA);
        // mContent = intent.getStringExtra(BIG_CONTENT_EXTRA);
    }

    private PendingIntent getNavigationPendingIntent(Context context) { //TODO CAMBIAR DESTINO
        return new NavDeepLinkBuilder(context).setComponentName(MainActivity.class)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.calendarFragment)
                .createPendingIntent();
    }

}
