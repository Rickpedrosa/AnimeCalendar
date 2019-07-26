package com.example.animecalendar;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.Objects;

public class App extends Application {
    public static final String CHANNEL_ONE = "channel_1";
    public static final int CHANNEL_ONE_INT = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Se crea el canal.
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ONE,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("la pruebita");
            // Se registra el canal en el gestor de notificaciones
            NotificationManager notificationManager =
                     getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);
        }
    }
}
