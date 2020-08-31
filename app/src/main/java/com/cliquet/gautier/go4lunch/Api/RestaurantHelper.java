package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    private static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static CollectionReference getUserSubcollection(String restaurantId) {
        return getRestaurantsCollection().document(restaurantId).collection("user");
    }

    public static void createRestaurant(Restaurant restaurant) {
        Map<String, Object> restaurantToCreate = new HashMap<>();

        restaurantToCreate.put("id", restaurant.getId());
        restaurantToCreate.put("name", restaurant.getName());
        restaurantToCreate.put("latitude", restaurant.getLatitude());
        restaurantToCreate.put("longitude", restaurant.getLongitude());
        restaurantToCreate.put("adress", restaurant.getAdress());
        restaurantToCreate.put("numberOfWorkmates", restaurant.getNumberOfWorkmates());
        restaurantToCreate.put("likedByUser", restaurant.getLikedByUser());
        restaurantToCreate.put("rating", restaurant.getRating());
        restaurantToCreate.put("selectedByUser", restaurant.getSelectedByUser());
        restaurantToCreate.put("phone", restaurant.getPhone());
        restaurantToCreate.put("websiteUrl", restaurant.getWebsiteUrl());
        restaurantToCreate.put("distance", restaurant.getDistance());
        restaurantToCreate.put("photoReference", restaurant.getPhotoReference());
        restaurantToCreate.put("openingHours", restaurant.getOpeningHours());

        RestaurantHelper.getRestaurantsCollection().document(restaurant.getId()).set(restaurantToCreate);
    }

    public static void addUserCollection(String restaurantId, String userId) {
        Map<String, Object> userToAdd = new HashMap<>();
        userToAdd.put("id", userId);
        RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).set(userToAdd);
    }

    public static Task<DocumentSnapshot> getRestaurant(String restaurantId) {
        return RestaurantHelper.getRestaurantsCollection().document(restaurantId).get();
    }

    public static void deleteUserInRestaurant(String restaurantId, String userId) {
        RestaurantHelper.getRestaurantsCollection().document(restaurantId).collection("user").document(userId).delete();
    }

    public static void deleteRestaurant(String restaurantId) {
        RestaurantHelper.getRestaurantsCollection().document(restaurantId).delete();
    }
}
