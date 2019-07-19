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

    private CustomTimeUtils() {
    }
}
