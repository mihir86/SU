package com.example.su;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;

public class SignInActivity extends AppCompatActivity {

    SignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_DARK);
        googleSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(signInWithGoogle()){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    //After backend for signin is implemented, call displaySignInFailedSnackbar() and delete the other code
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //TODO: Add backend code for Google sign in. Return true if sign in is successful and false if unsuccessful
    private boolean signInWithGoogle(){
        return false;
    }

    private void displaySignInFailedSnackbar(){
        Snackbar.make(findViewById(R.id.sign_in_layout), R.string.sign_in_failed, Snackbar.LENGTH_LONG).show();
    }
}
