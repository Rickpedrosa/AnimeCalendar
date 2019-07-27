package com.example.animecalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomTimeUtils {

    private static final String FORMAT = "dd/MM/yyyy";
    public static final long ONE_DAY_MILLISECONDS = 86400000L;
    public static final long ONE_MINUTE_MILLISECONDS = 60000L;
    private static final String FORMAT_HOUR = "dd/MM/yyyy HH:mm";

    public static String getDateFormatted(Date date) {
        return new SimpleDateFormat(FORMAT, Locale.getDefault()).format(date);
    }

    public static String getDateFormattedHour(long date) {
        return new SimpleDateFormat(FORMAT_HOUR, Locale.getDefault()).format(new Date(date));
    }

    public static long dateFromStringToLong(String date) throws ParseException {
        return new SimpleDateFormat(FORMAT, Locale.getDefault())
                .parse(date).getTime();
    }

    public static long dateFromStringToLongTime(String date) throws ParseException {
        return new SimpleDateFormat(FORMAT_HOUR, Locale.getDefault())
                .parse(date).getTime();
    }

    public static boolean isToday(String date) {
        return getDateFormatted(new Date()).equals(date);
    }

    public static String getWeekDay(String date) throws ParseException {
        return new SimpleDateFormat("EE", Locale.getDefault()).format(new Date(dateFromStringToLong(date)));
    }

    public static long getTodayWithTime(Integer mTime) throws ParseException {
        String today = getDateFormatted(new Date());
        return new SimpleDateFormat(FORMAT, Locale.getDefault()).
                parse(today).getTime() + (mTime * ONE_MINUTE_MILLISECONDS);
    }

    private CustomTimeUtils() {
    }
}
