package com.cliquet.gautier.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Go4LunchNotificationChannel extends Application {
    public static final String CHANNEL_TIME_TO_EAT = "picked restaurant reminder";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_TIME_TO_EAT,
                    "new articles channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notice if new articles are available");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
