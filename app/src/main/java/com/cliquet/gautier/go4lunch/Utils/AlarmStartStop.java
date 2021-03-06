package com.cliquet.gautier.go4lunch.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmStartStop {

    private AlarmManager mAlamManager;
    private PendingIntent mAlarmIntent;

    public void startAlarm(Context context) {
        this.setUpManager(context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 0);

        mAlamManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mAlarmIntent);
    }

    public void stopAlarm(Context context) {
        this.setUpManager(context);
        mAlamManager.cancel(mAlarmIntent);
    }

    private void setUpManager(Context context) {
        mAlamManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
