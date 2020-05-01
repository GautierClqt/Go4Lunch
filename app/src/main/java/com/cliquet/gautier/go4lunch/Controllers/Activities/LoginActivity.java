package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkForExistingAuthentication();

    }

    private void checkForExistingAuthentication() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        }
        else{
            startSignInActivity();
        }
    }

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_fulllogo_go4lunch)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                checkForExistingUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createNewUserInFirestore() {
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String fullName = firebaseUser.getDisplayName();
            String[] splitNames = fullName.split(" ");

            UserHelper.createUser(
                    firebaseUser.getUid(),
                    splitNames[0],
                    splitNames[1],
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl().toString()
            );
        }
        startMainActivity();
    }

    private void checkForExistingUser(String userId) {
        UserHelper.getUser(userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()) {
                    startMainActivity();
                }
                else {
                    createNewUserInFirestore();
                }
            }
        });
    }


}
