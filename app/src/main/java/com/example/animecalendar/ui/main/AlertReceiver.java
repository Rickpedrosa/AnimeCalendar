package com.example.animecalendar.ui.main;

import android.app.AlarmManager;
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
import com.example.animecalendar.utils.CustomTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.animecalendar.ui.main.MainActivity.CONTENT_EXTRA;
import static com.example.animecalendar.ui.main.MainActivity.TITLE_EXTRA;
import static com.example.animecalendar.utils.CustomTimeUtils.ONE_MINUTE_MILLISECONDS;

public class AlertReceiver extends BroadcastReceiver {

    public static final String CUSTOM_INTENT = "com.test.intent.action.ALARM";
    private String mTitle;
    private String mSubtitle;

    @Override
    public void onReceive(Context context, Intent intent) {
        /* enqueue the job */
       // MyJobIntentService.enqueueWork(context, intent);
        obtainArguments(intent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ONE)
                .setSmallIcon(R.drawable.ic_build_white_menu_24dp)
                .setContentTitle(mTitle)
                .setContentText(mSubtitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(getNavigationPendingIntent(context))
                .build();

        notificationManager.notify(App.CHANNEL_ONE_INT, notification);
        AlertReceiver.cancelAlarm(context);
    }

    public static void cancelAlarm(Context ctx) {
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /* cancel any pending alarm */
        Objects.requireNonNull(alarm).cancel(getPendingIntent(ctx));
    }

    public static void setAlarm(Context ctx) throws ParseException {
        cancelAlarm(ctx);
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /* fire the broadcast */
        Objects.requireNonNull(alarm).setExact(AlarmManager.RTC_WAKEUP, getAlarmTime(ctx), getPendingIntent(ctx));
    }

    private static PendingIntent getPendingIntent(Context ctx) {
        Intent alarmIntent = new Intent(ctx, AlertReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);
        alarmIntent.putExtra(TITLE_EXTRA, ctx.getResources().getString(R.string.app_name)); //title
        alarmIntent.putExtra(CONTENT_EXTRA, "Today animes!"); //content

        return PendingIntent.getBroadcast(ctx, 1, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static long getAlarmTime(Context ctx) throws ParseException {
        PreferenceManager preferenceManager = new PreferenceManager(ctx);
        int time = preferenceManager.getSharedPreferences().getInt(ctx.getResources().getString(R.string.time_notification_key), 90);

        String today = CustomTimeUtils.getDateFormatted(new Date());
        long schedule = CustomTimeUtils.getTodayWithTime(time);
        long todayComparison = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(today).getTime();

        Calendar cToday = Calendar.getInstance();
        cToday.setTime(new Date());
        int todayTime = (cToday.get(Calendar.HOUR_OF_DAY) * 60) + cToday.get(Calendar.MINUTE);
        todayComparison += todayTime * ONE_MINUTE_MILLISECONDS;

        if (schedule < todayComparison) {
            schedule = schedule + CustomTimeUtils.ONE_DAY_MILLISECONDS;
        }

        return schedule;
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