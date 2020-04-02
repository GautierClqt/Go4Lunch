package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static CollectionReference getUserCollection(String restaurantId) {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(restaurantId).collection("user");
    }

    //CREATE
    public static Task<Void> createRestaurant(String restaurantId) {
        Restaurant restautantToCreate = new Restaurant(restaurantId);
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).set(restautantToCreate);
    }

    public static Task<Void> addUserCollection(String restaurantId, String userId) {
        User userToAdd = new User(userId);
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).set(userToAdd);
    }

    public static Task<Void> deleteUser(String restaurantId, String userId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).delete();
    }

    public static Task<Void> deleteRestaurant(String restaurantId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).delete();
    }
}
