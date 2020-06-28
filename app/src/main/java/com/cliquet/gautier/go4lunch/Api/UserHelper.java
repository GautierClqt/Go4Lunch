package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //CREATE
    public static Task<Void> createUser(String userId, String userFirstName, String userLastName, String userEmail, String userUrlPicture) {
        User userToCreate = new User(userId, userFirstName, userLastName, userEmail, userUrlPicture);
        return UserHelper.getUsersCollection().document(userId).set(userToCreate);
    }

    public static Task<Void> addLikedRestaurantToSubcollection(String userId, String restaurantId) {
        HashMap<Object, String> likedRestaurantToCreate = new HashMap<>();
        likedRestaurantToCreate.put("id", restaurantId);
        return UserHelper.getUsersCollection().document(userId).collection("likedRestaurants").document(restaurantId).set(likedRestaurantToCreate);
    }

    //GET
    public static Task<DocumentSnapshot> getUser(String userId) {
        return UserHelper.getUsersCollection().document(userId).get();
    }

    public static CollectionReference getLikedRestaurants(String userId) {
        return getUsersCollection().document(userId).collection("likedRestaurants");
    }

    //UPDATE
    public static Task<Void> updateSelectedRestaurant(String userId, String restaurantId) {
        return UserHelper.getUsersCollection().document(userId).update("userSelected", restaurantId);
    }

    //DELETE
    public static Task<Void> deleteUser(String userId) {
        return UserHelper.getUsersCollection().document(userId).delete();
    }

    public static Task<Void> removeLikedRestaurantToSubcollection(String userId, String restaurantId) {
        return UserHelper.getUsersCollection().document(userId).collection("likedRestaurants").document(restaurantId).delete();
    }

    //######EN CHANTIER########
    public static Task<Void> updateSelectedRestaurant(String restaurantId, String userId, boolean selected) {
        return RestaurantHelper.getUserSubcollection(restaurantId).document(userId).update("selected", selected);
    }

    public static Task<Void> updateLikedRestaurant(String restaurantId, String userId, boolean liked) {
        return RestaurantHelper.getUserSubcollection(restaurantId).document(userId).update("liked", liked);
    }
}
