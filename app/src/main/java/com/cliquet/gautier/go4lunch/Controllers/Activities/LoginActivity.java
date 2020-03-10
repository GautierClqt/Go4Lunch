package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 123;

    SignInButton logInGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInGoogleButton = findViewById(R.id.activity_login_google_button);

        logInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });
    }

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int Test = 15;

        if(requestCode == GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                UserHelper.createUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), "fake_firstname", "fake_lastname", response.getEmail(), "fake_uripicture");
            }

            String email = response.getEmail();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            int TEST = 0;

            if(resultCode == RESULT_OK) {
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
            }
        }
    }
}
