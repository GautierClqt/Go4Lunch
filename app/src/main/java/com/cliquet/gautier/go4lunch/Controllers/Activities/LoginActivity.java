package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.cliquet.gautier.go4lunch.Api.UserHelper;
import com.cliquet.gautier.go4lunch.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 123;

    SignInButton logInGoogleButton;
    LoginButton loginFacebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInGoogleButton = findViewById(R.id.activity_login_google_button);
        loginFacebookButton = findViewById(R.id.activity_login_facebook_button);

        CallbackManager callbackManager = CallbackManager.Factory.create();

        logInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });

        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });

        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_logo_go4lunch)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                UserHelper.createUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), "fake_firstname", "fake_lastname", response.getEmail(), "fake_uripicture");
            }

            if(resultCode == RESULT_OK) {
                launchMainActivity();
            }
        }
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
