package com.example.su;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SignInActivity extends AppCompatActivity {

    private boolean signedInBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        signedInBefore = preferences.getBoolean(getString(R.string.signed_in_before_key), false);

        if (account != null) {
            if (signedInBefore) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            } else {
                Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.action_signInWithGoogleFragment_to_introFragment);
            }
        }

    }

}