package com.example.su;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class IntroActivity extends AppCompatActivity {

    FirebaseFirestore db;
    boolean userHasDetailsInDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        userHasDetailsInDatabase = false;
        db = FirebaseFirestore.getInstance();

        Button nextButton = findViewById(R.id.intro_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(getString(R.string.signed_in_before_key), true).apply();

                db.collection(getString(R.string.firebase_database_students_collection))
                        .whereEqualTo(getString(R.string.firebase_database_student_email_field), GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    userHasDetailsInDatabase = !task.getResult().isEmpty();
                                    if (userHasDetailsInDatabase) {
                                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(IntroActivity.this, EnterPersonalDetailsActivity.class));
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
    }

}
