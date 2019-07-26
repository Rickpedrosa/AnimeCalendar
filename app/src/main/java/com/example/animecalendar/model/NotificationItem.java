package com.example.animecalendar.model;

import java.util.List;

public class NotificationItem {

    private List<String> animeTitles;
    private int notificationTime;

    public NotificationItem(List<String> animeTitles, int notificationTime) {
        this.animeTitles = animeTitles;
        this.notificationTime = notificationTime;
    }

    public List<String> getAnimeTitles() {
        return animeTitles;
    }

    public void setAnimeTitles(List<String> animeTitles) {
        this.animeTitles = animeTitles;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }
}
