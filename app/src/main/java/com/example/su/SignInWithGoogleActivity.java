package com.example.su;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class SignInWithGoogleActivity extends AppCompatActivity {

    private final static int RC_SIGN_IN = 2;

    GoogleSignInClient mGoogleSignInClient;
    MaterialButton signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_sign_in_with_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                if (checkIfBITSEmailAndStoreDetails(account.getEmail())) {
                    startActivity(new Intent(SignInWithGoogleActivity.this, IntroActivity.class));
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.sign_in_use_bits_mail), Snackbar.LENGTH_SHORT).show();
                    mGoogleSignInClient.signOut();
                }
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.sign_in_failed), Snackbar.LENGTH_SHORT).show();
                mGoogleSignInClient.signOut();
            }

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        boolean signedInBefore = preferences.getBoolean(getString(R.string.signed_in_before_key), false);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null)
            if (signedInBefore) {
                startActivity(new Intent(SignInWithGoogleActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SignInWithGoogleActivity.this, IntroActivity.class));
                finish();
            }
    }

    private boolean checkIfBITSEmailAndStoreDetails(String email) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (email.toLowerCase().endsWith("@hyderabad.bits-pilani.ac.in")) {
            if (email.toLowerCase().startsWith("f20")) {
                editor.putString(getString(R.string.student_or_prof_key), getString(R.string.student_value));
                editor.apply();
            } else {
                editor.putString(getString(R.string.student_or_prof_key), getString(R.string.prof_value));
                editor.apply();
            }
            return true;
        } else if (email.equalsIgnoreCase("aryan.arora180@gmail.com") || email.equalsIgnoreCase("mstrmihir2@gmail.com") || email.equalsIgnoreCase("k.vineeth2000@gmail.com") || email.equalsIgnoreCase("abuch99@gmail.com")) {
            editor.putString(getString(R.string.student_or_prof_key), getString(R.string.prof_value));
            editor.apply();
            return true;
        } else
            return false;
    }

}
