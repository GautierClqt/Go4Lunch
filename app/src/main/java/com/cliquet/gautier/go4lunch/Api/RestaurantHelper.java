package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static CollectionReference getUserSubcollection(String restaurantId) {
        return getRestaurantsCollection().document(restaurantId).collection("user");
    }

    public static Task<Void> createRestaurant(Restaurant restaurant) {
        Map<String, Object> restaurantToCreate = new HashMap<>();

        restaurantToCreate.put("id", restaurant.getId());
        restaurantToCreate.put("name", restaurant.getName());
        restaurantToCreate.put("latitude", restaurant.getLatitude());
        restaurantToCreate.put("longitude", restaurant.getLongitude());
        restaurantToCreate.put("adress", restaurant.getAddress());
        restaurantToCreate.put("number_of_workmates", restaurant.getNumberOfWorkmates());
        restaurantToCreate.put("liked_by_user", restaurant.getLikedByUser());
        restaurantToCreate.put("rating", restaurant.getRating());
        restaurantToCreate.put("selected_by_user", restaurant.getSelectedByUser());
        restaurantToCreate.put("phone", restaurant.getPhone());
        restaurantToCreate.put("website_url", restaurant.getWebsiteUrl());
        restaurantToCreate.put("distance", restaurant.getDistance());
        restaurantToCreate.put("photo_reference", restaurant.getPhotoReference());
        restaurantToCreate.put("opening_hours", restaurant.getOpeningHoursString());

        return RestaurantHelper.getRestaurantsCollection().document(restaurant.getId()).set(restaurantToCreate);
    }

    public static Task<Void> addUserCollection(String restaurantId, String userId) {
        Map<String, Object> userToAdd = new HashMap<>();
        userToAdd.put("id", userId);
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).set(userToAdd);
    }

    public static Task<DocumentSnapshot> getRestaurant(String restaurantId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).get();
    }

    public static Task<Void> deleteUser(String restaurantId, String userId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).delete();
    }

    public static Task<Void> deleteRestaurant(String restaurantId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).delete();
    }
}
