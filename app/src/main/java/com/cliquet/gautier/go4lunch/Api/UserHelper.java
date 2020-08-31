package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //CREATE
    public static void createUser(String userId, String userFirstName, String userLastName, String userEmail, String userUrlPicture) {
        User userToCreate = new User(userId, userFirstName, userLastName, userEmail, userUrlPicture);
        UserHelper.getUsersCollection().document(userId).set(userToCreate);
    }

    public static void addLikedRestaurantToSubcollection(String userId, String restaurantId) {
        HashMap<Object, String> likedRestaurantToCreate = new HashMap<>();
        likedRestaurantToCreate.put("id", restaurantId);
        UserHelper.getUsersCollection().document(userId).collection("likedRestaurants").document(restaurantId).set(likedRestaurantToCreate);
    }

    //GET
    public static Task<DocumentSnapshot> getUser(String userId) {
        return UserHelper.getUsersCollection().document(userId).get();
    }

    public static CollectionReference getLikedRestaurants(String userId) {
        return getUsersCollection().document(userId).collection("likedRestaurants");
    }

    //UPDATE
    public static void updateSelectedRestaurant(String userId, String restaurantId) {
        UserHelper.getUsersCollection().document(userId).update("userSelected", restaurantId);
    }

    public static void removeLikedRestaurantToSubcollection(String userId, String restaurantId) {
        UserHelper.getUsersCollection().document(userId).collection("likedRestaurants").document(restaurantId).delete();
    }
}
