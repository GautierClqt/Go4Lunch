package com.cliquet.gautier.go4lunch.Utils;

import android.app.Notification;
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
import java.util.Objects;

import static com.cliquet.gautier.go4lunch.Go4LunchNotificationChannel.CHANNEL_TIME_TO_EAT;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    SharedPreferences preferences;
    NotificationManagerCompat notificationManager;
    Gson gson = new Gson();

    String mUserName;
    String mRestaurantName = null;
    String mRestaurantAddress = null;
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
                if(Objects.requireNonNull(task.getResult()).getString("userSelected") != null) {
                    String selectedRestaurant = Objects.requireNonNull(task.getResult().get("userSelected")).toString();
                    getSelectedRestaurantInFirestore(selectedRestaurant);
                }
                else {
                    createNotificationString();
                }
            }
        });
    }

    private void getSelectedRestaurantInFirestore(final String selectedRestaurant) {
        RestaurantHelper.getRestaurant(selectedRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mRestaurantName = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).get("name")).toString();
                if(task.getResult().get("address") != null) {
                    mRestaurantAddress = Objects.requireNonNull(task.getResult().get("address")).toString();
                } else {
                    mRestaurantAddress = null;
                }

                final List<String> workmatesIdList = new ArrayList<>();

                RestaurantHelper.getUserSubcollection(selectedRestaurant).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                                if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(documentSnapshots.getId())) {
                                    workmatesIdList.add(documentSnapshots.getId());
                                }
                            }
                            if (workmatesIdList.size() > 0) {
                                getWorkmatesInFirestore(workmatesIdList);

                            } else {
                                createNotificationString();
                            }
                        }
                    }
                });
            }
        });
    }

    private void getWorkmatesInFirestore(final List<String> workmatesIdList) {
        for(int i = 0; i<workmatesIdList.size(); i++) {
            if (!workmatesIdList.get(i).equals(FirebaseAuth.getInstance().getUid())) {
                UserHelper.getUser(workmatesIdList.get(i)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Workmates workmates = Objects.requireNonNull(task.getResult()).toObject(Workmates.class);
                        assert workmates != null;
                        mWorkmatesNameList.add(workmates.getFirstName());
                        if (mWorkmatesNameList.size() == workmatesIdList.size()) {
                            createNotificationString();
                        }
                    }
                });
            }
        }
    }

    private void createNotificationString() {
        StringBuilder notificationString;

        if(mRestaurantName != null) {
            notificationString = new StringBuilder(mRestaurantName);
            if(mRestaurantAddress != null) {
                notificationString.append("\n").append(mRestaurantAddress);
            }
            else {
                notificationString.append("\n").append(context.getString(R.string.notif_no_address));
            }
            if (mWorkmatesNameList.size() > 0) {
                int listSize = mWorkmatesNameList.size();
                notificationString.append("\n").append(context.getString(R.string.notif_join_by)).append(" ");
                for (int i = 0; i < listSize; i++) {
                    notificationString.append(mWorkmatesNameList.get(i));
                    if (i < listSize - 2) {
                        notificationString.append(", ");
                    } else if (i == listSize - 2) {
                        notificationString.append(" ").append(context.getString(R.string.notif_and)).append(" ");
                    }
                }
            }
            notificationString.append("\n\n").append(context.getString(R.string.notif_have_a_good_lunch));
        }
        else {
            notificationString = new StringBuilder(context.getString(R.string.notif_no_restaurant_pick));
        }
        sendNotification(notificationString.toString());
    }

    private void sendNotification(String notificationString) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_TIME_TO_EAT)
                .setSmallIcon(R.drawable.ic_logo_go4lunch)
                .setContentTitle("Go4Lunch")
                .setContentText(context.getString(R.string.notif_content_text))
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notificationString))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(0, notification);
    }

    private void setPreferences() {
        preferences = context.getSharedPreferences("Go4Lunch_Settings", Context.MODE_PRIVATE);
    }
}