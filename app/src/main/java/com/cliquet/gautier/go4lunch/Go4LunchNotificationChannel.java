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
                    "tome to eat channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Remind user the restaurant he wants to eat to and which workmates will join him");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
