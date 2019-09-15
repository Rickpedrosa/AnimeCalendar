package com.example.animecalendar.utils;

import com.example.animecalendar.model.NotificationItem;

import java.text.ParseException;
import java.util.Calendar;

public class ValidationUtils {

    public static boolean getAlarmNotificationWard(NotificationItem notif) {
        return notif.getAnimeTitles().size() > 0 && notif.isNotificationAccess()
                && notif.getNotificationTime() != 0;
    }

    public static boolean isEqualDate(String episode){
        return CustomTimeUtils.getDateFormatted(Calendar.getInstance().getTime())
                .equals(episode);
    }

    public static boolean isMinorDate(String episode) throws ParseException {
        return CustomTimeUtils.dateFromStringToLong(episode)
                < Calendar.getInstance().getTime().getTime();
    }

    public static boolean assignmentController(int days, int caps) {
        return caps >= days && days > 0;
    }


    private ValidationUtils() {
    }
}
