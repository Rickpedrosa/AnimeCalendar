package com.example.animecalendar.utils;

import com.example.animecalendar.model.MyAnimeEpisodesList;
import com.example.animecalendar.model.NotificationItem;

import java.text.ParseException;
import java.util.Calendar;

public class ValidationUtils {

    public static boolean getAlarmNotificationWard(NotificationItem notif) {
        return notif.getAnimeTitles().size() > 0 && notif.isNotificationAccess()
                && notif.getNotificationTime() != 0;
    }

    public static boolean isEqualDate(MyAnimeEpisodesList episode){
        return CustomTimeUtils.getDateFormatted(Calendar.getInstance().getTime())
                .equals(episode.getWatchToDate());
    }

    public static boolean isMinorDate(MyAnimeEpisodesList episode) throws ParseException {
        return CustomTimeUtils.dateFromStringToLong(episode
                .getWatchToDate())
                < Calendar.getInstance().getTime().getTime();
    }

    private ValidationUtils() {
    }
}
