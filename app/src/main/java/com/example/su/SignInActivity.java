package com.example.su;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    SignInButton googleSignInButton;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    GoogleSignInClient mGoogleSignInClient;

    private boolean signedInBefore;

    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            if (signedInBefore)
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            else
                startActivity(new Intent(SignInActivity.this, IntroActivity.class));

        } else {
            googleSignInButton.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        signedInBefore = preferences.getBoolean(getString(R.string.signed_in_before_key), false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.auth_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_DARK);
        googleSignInButton.setVisibility(View.INVISIBLE);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                if (checkIfBITSEmailAndStoreDetails(account.getEmail())) {
                    if (signedInBefore)
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    else
                        startActivity(new Intent(SignInActivity.this, IntroActivity.class));
                } else {
                    mGoogleSignInClient.revokeAccess();
                    Snackbar.make(findViewById(R.id.sign_in_layout), getString(R.string.sign_in_use_bits_mail), Snackbar.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.sign_in_layout), getString(R.string.sign_in_failed), Snackbar.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.sign_in_layout), getString(R.string.sign_in_failed), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
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
        } else if (email.equalsIgnoreCase("aryan.arora180@gmail.com") || email.equalsIgnoreCase("mstrmihir2@gmail.com")) {
            editor.putString(getString(R.string.student_or_prof_key), getString(R.string.prof_value));
            editor.apply();
            return true;
        } else
            return false;
    }
}