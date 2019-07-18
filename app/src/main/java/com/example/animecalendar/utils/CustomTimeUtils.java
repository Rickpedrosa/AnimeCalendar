package com.example.animecalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomTimeUtils {

    public static String getDateFormatted(Date date) {
        return new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(date);
    }

    public static long dateFromStringToTimestamp(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).parse(date).getTime();
    }

    private CustomTimeUtils() {
    }
}
