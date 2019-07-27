package com.example.animecalendar.model;

import java.util.List;

public class NotificationItem {

    private List<String> animeTitles;
    private int notificationTime;
    private boolean notificationAccess;

    public NotificationItem(List<String> animeTitles, int notificationTime, boolean notificationAccess) {
        this.animeTitles = animeTitles;
        this.notificationTime = notificationTime;
        this.notificationAccess = notificationAccess;
    }

    public boolean isNotificationAccess() {
        return notificationAccess;
    }

    public void setNotificationAccess(boolean notificationAccess) {
        this.notificationAccess = notificationAccess;
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
