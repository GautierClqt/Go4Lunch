package com.cliquet.gautier.go4lunch.Api;

import com.cliquet.gautier.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public static Task<DocumentSnapshot> getUser(String userId) {
        return UserHelper.getUsersCollection().document(userId).get();
    }

    //DELETE
    public static Task<Void> deleteUser(String userId) {
        return UserHelper.getUsersCollection().document(userId).delete();
    }
}
