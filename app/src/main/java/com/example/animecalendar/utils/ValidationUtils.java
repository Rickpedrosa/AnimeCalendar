package com.example.animecalendar.utils;

import com.example.animecalendar.model.NotificationItem;

public class ValidationUtils {

    public static boolean getAlarmNotificationWard(NotificationItem notif) {
        return notif.getAnimeTitles().size() > 0 && notif.isNotificationAccess()
                && notif.getNotificationTime() != 0;
    }

    private ValidationUtils() {
    }
}
