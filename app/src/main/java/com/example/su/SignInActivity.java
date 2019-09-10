package com.example.su;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                    //Move to the next Activity
                }
                else{
                    displaySignInFailedSnackbar();
                }
            }
        });
    }

    //This method is called when the "sign in with Google" button is tapped. Add the authentication code here. Return false if sign in fails and a dialog will be shown saying that the sign in failed.
    private boolean signInWithGoogle(){
        return false;
    }

    private void displaySignInFailedSnackbar(){
        Snackbar.make(findViewById(R.id.sign_in_layout), R.string.sign_in_failed, Snackbar.LENGTH_LONG).show();
    }
}
