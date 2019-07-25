package com.example.animecalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomTimeUtils {

    private static final String FORMAT = "dd/MM/yyyy";
    public static final long ONE_DAY_MILLISECONDS = 86400000L;

    public static String getDateFormatted(Date date) {
        return new SimpleDateFormat(FORMAT, Locale.getDefault()).format(date);
    }

    public static long dateFromStringToLong(String date) throws ParseException {
        return new SimpleDateFormat(FORMAT, Locale.getDefault())
                .parse(date).getTime();
    }

    public static boolean isToday(String date) {
        return getDateFormatted(new Date()).equals(date);
    }

    public static String getWeekDay(String date) throws ParseException {
        return new SimpleDateFormat("EE", Locale.getDefault()).format(new Date(dateFromStringToLong(date)));
    }

    public static String getToday() {
        return getDateFormatted(new Date());
    }

    private CustomTimeUtils() {
    }
}
