package com.cliquet.gautier.go4lunch.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    SharedPreferences preferences;
    NotificationManagerCompat notificationManager;
    Gson gson = new Gson();

    Boolean switchPosition;
    String jsonCheckboxIdList;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        setPreferences();

        notificationManager = NotificationManagerCompat.from(context);

        switchPosition = preferences.getBoolean("switch_position", true);
        if(switchPosition) {
            jsonCheckboxIdList = preferences.getString("checkboxes_id_list", null);
            if(jsonCheckboxIdList != null) {
                ArrayList<Integer> checkboxIdList = gson.fromJson(jsonCheckboxIdList, new TypeToken<ArrayList<Integer>>(){}.getType());
                //Récupérer le jour de la semaine actuel et vérifier l'état de la poisition de checkboxIdList correspondant(si null pas de notirf, si non null activer la notif)
            }
        }
    }

    private void setPreferences() {
        preferences = context.getSharedPreferences("Go4Lunch_Settings", Context.MODE_PRIVATE);
    }
}