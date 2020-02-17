package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cliquet.gautier.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    Button logInGoogleButton;
    Button logInFacebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInGoogleButton = findViewById(R.id.activity_login_google_button);

        logInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                .setLogo(R.drawable.ic_logo_go4lunch)
                                .build(),
                        RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
