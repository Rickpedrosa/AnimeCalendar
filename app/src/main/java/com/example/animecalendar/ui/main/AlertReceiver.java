package com.example.animecalendar.ui.main;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.animecalendar.App;
import com.example.animecalendar.R;
import com.example.animecalendar.ui.days.DaysFragmentDirections;
import com.example.animecalendar.ui.days_episodes.DaysEpisodesFragmentArgs;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.animecalendar.ui.main.MainActivity.BIG_CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.TITLE_EXTRA;
import static com.example.animecalendar.utils.CustomTimeUtils.ONE_MINUTE_MILLISECONDS;

public class AlertReceiver extends BroadcastReceiver {

    public static final String CUSTOM_INTENT = "com.test.intent.action.ALARM";
    private String mTitle;
    private String mSubtitle;
    private String mContent;

    @Override
    public void onReceive(Context context, Intent intent) {
        obtainArguments(intent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ONE)
                .setSmallIcon(R.drawable.ic_stat_call_white)
                .setContentTitle(mTitle)
                .setContentText(mSubtitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mContent))
                .setContentIntent(getNavigationPendingIntent(context))
                .setAutoCancel(true)
                .build();

        notificationManager.notify(App.CHANNEL_ONE_INT, notification);
        AlertReceiver.cancelAlarm(context);
    }

    public static void cancelAlarm(Context ctx) {
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /* cancel any pending alarm */
        Objects.requireNonNull(alarm).cancel(getPendingIntent(ctx, ""));
    }

    public static void setAlarm(Context ctx, String bigContent, int time) throws ParseException {
        cancelAlarm(ctx);
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /* fire the broadcast */
        Objects.requireNonNull(alarm).set(
                AlarmManager.RTC_WAKEUP,
                getAlarmTime(time),
                getPendingIntent(ctx, bigContent));
    }

    private static PendingIntent getPendingIntent(Context ctx, String bigContent) {
        Intent alarmIntent = new Intent(ctx, AlertReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);
        alarmIntent.putExtra(TITLE_EXTRA, ctx.getResources().getString(R.string.app_name)); //title
        alarmIntent.putExtra(CONTENT_EXTRA, ctx.getResources().getString(R.string.notif_title)); //content
        alarmIntent.putExtra(BIG_CONTENT_EXTRA, bigContent);

        return PendingIntent.getBroadcast(ctx, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static long getAlarmTime(int time) throws ParseException {
        Calendar cToday = Calendar.getInstance();
        cToday.setTime(new Date());
        int todayTime = (cToday.get(Calendar.HOUR_OF_DAY) * 60) + cToday.get(Calendar.MINUTE);

        long todayComparison = CustomTimeUtils.getTodayWithTime(todayTime);
        long schedule = CustomTimeUtils.getTodayWithTime(time);

        if (schedule < todayComparison) {
            schedule = schedule + CustomTimeUtils.ONE_DAY_MILLISECONDS;
        }

        return schedule;
    }

    private void obtainArguments(Intent intent) {
        mTitle = intent.getStringExtra(TITLE_EXTRA);
        mSubtitle = intent.getStringExtra(CONTENT_EXTRA);
        mContent = intent.getStringExtra(BIG_CONTENT_EXTRA);
    }

    private PendingIntent getNavigationPendingIntent(Context context) {
        Bundle bundle = new Bundle(); // date key same in the navigation args
        bundle.putString("date", CustomTimeUtils.getDateFormatted(new Date()));
        return new NavDeepLinkBuilder(context).setComponentName(MainActivity.class)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.daysEpisodeFragment)
                .setArguments(bundle)
                .createPendingIntent();
    }
}