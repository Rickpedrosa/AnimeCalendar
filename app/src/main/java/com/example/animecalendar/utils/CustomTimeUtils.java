package com.example.animecalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CustomTimeUtils {

    private static final String FORMAT = "yyyy/MM/dd";
    public static final long ONE_DAY_MILLISECONDS = 86400000L;
    private static final long ONE_MINUTE_MILLISECONDS = 60000L;
//    private static final String FORMAT_HOUR = "dd/MM/yyyy HH:mm";

    public static String getDateFormatted(Date date) {
        return new SimpleDateFormat(FORMAT, Locale.getDefault()).format(date);
    }

    public static long dateFromStringToLong(String date) throws ParseException {
        return Objects.requireNonNull(new SimpleDateFormat(FORMAT, Locale.getDefault())
                .parse(date)).getTime();
    }

    public static boolean isToday(String date) {
        return getDateFormatted(new Date()).equals(date);
    }

    public static String getWeekDay(String date) throws ParseException {
        return new SimpleDateFormat("EE", Locale.getDefault()).format(new Date(dateFromStringToLong(date)));
    }

    public static long getTodayWithTime(Integer mTime) throws ParseException {
        String today = getDateFormatted(new Date());
        return Objects.requireNonNull(new SimpleDateFormat(FORMAT, Locale.getDefault()).
                parse(today)).getTime() + (mTime * ONE_MINUTE_MILLISECONDS);
    }

    private CustomTimeUtils() {
    }
}
