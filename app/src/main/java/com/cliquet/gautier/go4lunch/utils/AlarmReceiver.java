package com.cliquet.gautier.go4lunch.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cliquet.gautier.go4lunch.Api.RestaurantHelper;
import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.cliquet.gautier.go4lunch.Go4LunchNotificationChannel.CHANNEL_TIME_TO_EAT;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    SharedPreferences preferences;
    NotificationManagerCompat notificationManager;
    Gson gson = new Gson();

    String mUserName;
    String mRestaurantName;
    String mRestaurantAddress;
    ArrayList<String> mWorkmatesNameList = new ArrayList<>();
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

                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
                if(checkboxIdList.get(currentDay-1) != null) {
                    executeFirestoreRequests();
                }
            }
        }
    }

    private void executeFirestoreRequests() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mUserName = auth.getUid();

        UserHelper.getUser(mUserName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().getString("userSelected") != null) {
                    String selectedRestaurant = task.getResult().get("userSelected").toString();
                    getSelectedRestaurantInFirestore(selectedRestaurant);
                }
            }
        });
    }

    private void getSelectedRestaurantInFirestore(final String selectedRestaurant) {
        RestaurantHelper.getRestaurant(selectedRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mRestaurantName = task.getResult().get("name").toString();
                mRestaurantAddress = task.getResult().get("address").toString();
                final List<String> workmatesIdList = new ArrayList<>();

                RestaurantHelper.getUserSubcollection(selectedRestaurant).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                            if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(documentSnapshots.getId())) {
                                workmatesIdList.add(documentSnapshots.getId());
                            }
                        }
                        getWorkmatesInFirestore(workmatesIdList);
                    }
                });
            }
        });
    }

    private void getWorkmatesInFirestore(final List<String> workmatesIdList) {
        for(int i = 0; i<workmatesIdList.size(); i++) {
            UserHelper.getUser(workmatesIdList.get(i)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Workmates workmate = task.getResult().toObject(Workmates.class);
                    mWorkmatesNameList.add(workmate.getFirstName());
                    if(mWorkmatesNameList.size() == workmatesIdList.size()) {
                        createNotificationStrin();
                    }
                }
            });
        }
    }

    private void createNotificationStrin() {
        String notificationString;

        notificationString = mUserName;
        notificationString = notificationString + "\n" + mRestaurantName;
        notificationString = notificationString + "\n" + mRestaurantAddress;
        if(mWorkmatesNameList != null ) {
            notificationString = notificationString + "\n" + "You will be join by: ";
            for (int i = 0; i < mWorkmatesNameList.size(); i++) {
                notificationString = notificationString + mWorkmatesNameList.get(i) + ",";
            }
        }
        sendNotification(notificationString);
    }

    private void sendNotification(String notificationString) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_TIME_TO_EAT)
                .setSmallIcon(R.drawable.ic_logo_go4lunch)
                .setContentTitle("Go4Lunch")
                .setContentText(notificationString)
                .setContentText("TEXTE A CREER")
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        notificationManager.notify(0, notification);
    }

    private void setPreferences() {
        preferences = context.getSharedPreferences("Go4Lunch_Settings", Context.MODE_PRIVATE);
    }
}