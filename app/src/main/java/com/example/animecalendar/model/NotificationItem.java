package com.example.animecalendar.model;

import java.util.List;

public class NotificationItem {

    private List<String> animeTitles;
    private long notificationTime;

    public NotificationItem(List<String> animeTitles, long notificationTime) {
        this.animeTitles = animeTitles;
        this.notificationTime = notificationTime;
    }

    public List<String> getAnimeTitles() {
        return animeTitles;
    }

    public void setAnimeTitles(List<String> animeTitles) {
        this.animeTitles = animeTitles;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }
}
