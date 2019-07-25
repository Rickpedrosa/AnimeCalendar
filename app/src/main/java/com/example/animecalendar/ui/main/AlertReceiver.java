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

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ONE)
                .setSmallIcon(R.drawable.ic_build_white_menu_24dp)
                .setContentTitle("Prueba1")
                .setContentText(intent.getStringExtra("KEY"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(new NavDeepLinkBuilder(context).setComponentName(MainActivity.class)
                        .setGraph(R.navigation.main_navigation)
                        .setDestination(R.id.calendarFragment)
                        .createPendingIntent())
                .build();
        notificationManager.notify(1, notification);
    }
}
