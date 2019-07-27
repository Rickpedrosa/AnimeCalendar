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
import com.example.animecalendar.base.MyJobIntentService;
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

public class AlertReceiver extends BroadcastReceiver {

    public static final String CUSTOM_INTENT = "com.test.intent.action.ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        /* enqueue the job */
        MyJobIntentService.enqueueWork(context, intent);
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
        long schedule = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(today).getTime()
                + (time * 60000L);
        long todayComparison = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(today).getTime();

        Calendar cToday = Calendar.getInstance();
        cToday.setTime(new Date());
        int todayTime = (cToday.get(Calendar.HOUR_OF_DAY) * 60) + cToday.get(Calendar.MINUTE);
        todayComparison += todayTime * 60000L;

        if (schedule < todayComparison) {
            schedule = schedule + CustomTimeUtils.ONE_DAY_MILLISECONDS;
        }

        return schedule;
    }
}