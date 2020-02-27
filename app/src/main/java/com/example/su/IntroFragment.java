package com.example.su;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class IntroFragment extends Fragment {

    FirebaseFirestore db;
    boolean userHasDetailsInDatabase;
    String email;

    public IntroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intro, container, false);

        userHasDetailsInDatabase = false;
        db = FirebaseFirestore.getInstance();

        Button nextButton = rootView.findViewById(R.id.intro_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(getString(R.string.signed_in_before_key), true).apply();

                db.collection(getString(R.string.firebase_database_students_collection))
                        .whereEqualTo(getString(R.string.firebase_database_student_email_field), FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    userHasDetailsInDatabase = !task.getResult().isEmpty();
                                    if (userHasDetailsInDatabase) {
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    } else {
                                        //TODO: Open EnterPersonalDetailsFragment through navigation controller
                                    }
                                }
                            }
                        });
            }
        });

        return rootView;
    }

}
