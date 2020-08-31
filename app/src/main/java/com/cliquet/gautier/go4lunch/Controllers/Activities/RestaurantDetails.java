package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cliquet.gautier.go4lunch.Api.RestaurantHelper;
import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.Controllers.Fragments.WorkmatesRecyclerAdapter;
import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetails extends AppCompatActivity {

    final String USER_ID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    SharedPreferences preferences;

    Restaurant mRestaurant;
    String mRestaurantId;
    WorkmatesRecyclerAdapter adapter = new WorkmatesRecyclerAdapter(this);

    TextView name;
    TextView address;
    ImageView firstStar;
    ImageView secondStar;
    ImageView thirdStar;
    Button selected;
    Button phone;
    Button likedByUser;
    Button website;
    ImageView picture;
    RecyclerView recyclerView;

    ArrayList<Workmates> mJoiningWorkmatesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        preferences = getSharedPreferences("Go4Lunch_Your_Lunch", MODE_PRIVATE);

        initViews();
        checkIsLiked();

        mRestaurant = getIntent().getParcelableExtra("restaurant");

        name.setText(mRestaurant.getName());
        name.setTypeface(Typeface.DEFAULT_BOLD);

        address.setText(mRestaurant.getAdress());

        firstStar.setVisibility(View.INVISIBLE);
        secondStar.setVisibility(View.INVISIBLE);
        thirdStar.setVisibility(View.INVISIBLE);

        //display stars according to the number of likes
        if(mRestaurant.getRating() > 0) {
            firstStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurant.getRating() > 1) {
            secondStar.setVisibility(View.VISIBLE);
        }
        if(mRestaurant.getRating() > 2) {
            thirdStar.setVisibility(View.VISIBLE);
        }

        final String photoReference = mRestaurant.getPhotoReference();
        if(photoReference != null) {
            String apiKey = getString(R.string.google_maps_key);
            Glide.with(picture).load("https://maps.googleapis.com/maps/api/place/photo?key=" + apiKey + "&photoreference=" + photoReference + "&maxwidth=600").into(picture);
        } else {
            Glide.with(picture).load(R.drawable.default_restaurant_picture).into(picture);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRestaurant.getPhone() == null || mRestaurant.getPhone().equals("")) {
                    Toast.makeText(getApplicationContext(), "no phone number found", Toast.LENGTH_SHORT).show();
                }
                else {
                    startCallActivity();
                }
            }
        });

        likedByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference likedRestaurantsRef = UserHelper.getLikedRestaurants(USER_ID);

                likedRestaurantsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                        if(documentSnapshots.isEmpty()) {
                            UserHelper.addLikedRestaurantToSubcollection(USER_ID, mRestaurant.getId());
                            likedByUser.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.details_liked_true_40dp, 0, 0);
                        }
                        for(int i = 0; i < documentSnapshots.size(); i++) {
                            if(documentSnapshots.get(i).getId().equals(mRestaurant.getId())) {
                                UserHelper.removeLikedRestaurantToSubcollection(USER_ID, mRestaurant.getId());
                                likedByUser.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.details_liked_false_40dp, 0, 0);
                                break;
                            }
                            else {
                                UserHelper.addLikedRestaurantToSubcollection(USER_ID, mRestaurant.getId());
                                likedByUser.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.details_liked_true_40dp, 0, 0);
                            }
                        }
                    }
                });
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRestaurant.getWebsiteUrl() == null || mRestaurant.getWebsiteUrl().equals("")) {
                    Toast.makeText(getApplicationContext(), "no website found", Toast.LENGTH_SHORT).show();
                }
                else {
                    startWebsiteActivity();
                }
            }
        });

        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlingFirestoreRequestsAndUi();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CollectionReference usersRef = RestaurantHelper.getUserSubcollection(mRestaurant.getId());

        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                getJoiningWorkmatesInFirestore();
            }
        });
    }

    private void checkIsLiked() {
        CollectionReference likedRestaurantsRef = UserHelper.getLikedRestaurants(USER_ID);

        likedRestaurantsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                for(int i = 0; i < documentSnapshots.size(); i++) {
                    if(documentSnapshots.get(i).getId().equals(mRestaurant.getId())) {
                        likedByUser.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.details_liked_true_40dp, 0, 0);
                        break;
                    }
                }
            }
        });
    }

    private void setWorkmatesAdapter() {
        adapter.setWorkmatesList(mJoiningWorkmatesList);
        adapter.setActiveActivity(2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getJoiningWorkmatesInFirestore() {
        final List<String> workmatesIdList = new ArrayList<>();

        RestaurantHelper.getUserSubcollection(mRestaurant.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                    if(!USER_ID.equals(documentSnapshots.getId())) {
                        workmatesIdList.add(documentSnapshots.getId());
                    }
                    else {
                        selected.setBackgroundResource(R.drawable.ic_check_circle_selected_60dp);
                    }
                }
                getWorkmatesDatasInFirestore(workmatesIdList);
            }
        });
    }

    private void getWorkmatesDatasInFirestore(List<String> workmatesIdList) {
        mJoiningWorkmatesList.clear();
        for(int i = 0; i<workmatesIdList.size(); i++) {
            UserHelper.getUser(workmatesIdList.get(i)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Workmates workmates = task.getResult().toObject(Workmates.class);
                    DocumentSnapshot doc = task.getResult();

                    String selectedRestaurant;
                    if(doc.get("userSelected") == null) {
                        mRestaurantId = null;
                    }
                    else {
                        selectedRestaurant = doc.get("userSelected").toString();
                        RestaurantHelper.getRestaurant(selectedRestaurant).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                mRestaurantId = task.getResult().toObject(Restaurant.class).getId();
                            }
                        });
                    }

                    mJoiningWorkmatesList.add(new Workmates(
                            workmates.getId(),
                            workmates.getFirstName(),
                            workmates.getLastName(),
                            workmates.getEmail(),
                            workmates.getUrlPicture(),
                            workmates.getSelectedRestaurant()
                    ));
                    setWorkmatesAdapter();
                }
            });
        }
    }

//    private void checkForRestaurantInFirestore(final String restaurantId) {
//        RestaurantHelper.getRestaurant(restaurantId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                RestaurantHelper.createRestaurant(restaurantId, mRestaurant.getName(), mRestaurant.getAdress());
//                RestaurantHelper.addUserCollection(restaurantId, USER_ID);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                RestaurantHelper.createRestaurant(restaurantId, mRestaurant.getName(), mRestaurant.getAdress());
//            }
//        });
//    }

    private void handlingFirestoreRequestsAndUi() {
        final String newRestaurantId = mRestaurant.getId();

        RestaurantHelper.createRestaurant(mRestaurant);

        UserHelper.getUser(USER_ID).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    final String oldRestaurantId = document.getString("userSelected");

                    if(oldRestaurantId == null){
                        UserHelper.updateSelectedRestaurant(USER_ID, mRestaurant.getId());
                        RestaurantHelper.addUserCollection(newRestaurantId, USER_ID);
                        selected.setBackgroundResource(R.drawable.ic_check_circle_selected_60dp);
                        saveYourLunch();
                    }
                    else if(newRestaurantId.equals(oldRestaurantId)) {
                        UserHelper.updateSelectedRestaurant(USER_ID, null);
                        RestaurantHelper.deleteUserInRestaurant(oldRestaurantId, USER_ID);
                        checkAndDeleteEmptyRestaurant(oldRestaurantId);
                        selected.setBackgroundResource(R.drawable.ic_check_circle_unselected_60dp);
                        deleteYourLunch();
                    }
                    else {
                        UserHelper.updateSelectedRestaurant(USER_ID, mRestaurant.getId());
                        RestaurantHelper.deleteUserInRestaurant(oldRestaurantId, USER_ID);
                        RestaurantHelper.addUserCollection(newRestaurantId, USER_ID);
                        checkAndDeleteEmptyRestaurant(oldRestaurantId);
                        selected.setBackgroundResource(R.drawable.ic_check_circle_selected_60dp);
                        saveYourLunch();
                    }
                }
            }
        });
    }

    private void saveYourLunch() {
        Gson gson = new Gson();
        String gsonYourLunch = gson.toJson(mRestaurant);
        preferences.edit().putString("your_lunch", gsonYourLunch).apply();
    }

    private void deleteYourLunch() {
        preferences.edit().remove("your_lunch").apply();
    }

    private void checkAndDeleteEmptyRestaurant(final String oldRestaurantId) {
        CollectionReference restaurantRef = RestaurantHelper.getUserSubcollection(oldRestaurantId);
        restaurantRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().isEmpty()) {
                        RestaurantHelper.deleteRestaurant(oldRestaurantId);
                    }
                }
            }
        });
    }

    private void startCallActivity() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mRestaurant.getPhone()));
        startActivity(intent);
    }

    private void startWebsiteActivity() {
        Intent intent = new Intent(this, WebsiteActivity.class);
        intent.putExtra("url", mRestaurant.getWebsiteUrl());
        startActivity(intent);
    }

    private void initViews() {
        name = findViewById(R.id.activity_restaurant_details_name_textview);
        address = findViewById(R.id.activity_restaurant_details_address_textview);
        firstStar = findViewById(R.id.activity_restaurant_details_firststar_imageview);
        secondStar = findViewById(R.id.activity_restaurant_details_secondstar_imageview);
        thirdStar = findViewById(R.id.activity_restaurant_details_thirdstar_imageview);
        selected = findViewById(R.id.activity_restaurant_details_selectrestaurant_button);
        phone = findViewById(R.id.activity_restaurant_details_phone_button);
        likedByUser = findViewById(R.id.activity_restaurant_details_likebyuser_button);
        website = findViewById(R.id.activity_restaurant_details_website_button);
        picture = findViewById(R.id.activity_restaurant_details_picture_imageview);
        recyclerView = findViewById(R.id.activity_restaurant_details_workmates_recyclerview);
    }
}
