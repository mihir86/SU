package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateAvailabilityFragment extends Fragment {

    CardView markAvailable;
    CardView markUnavailable;

    private FirebaseFirestore db;

    public UpdateAvailabilityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_availability_update, container, false);

        markAvailable = rootView.findViewById(R.id.mark_available_card_view);
        markUnavailable = rootView.findViewById(R.id.mark_unavailable_card_view);

        db = FirebaseFirestore.getInstance();

        markAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String emailid = user.getEmail();

                db.collection(getString(R.string.firebase_database_professor_collection))
                        .whereEqualTo(getString(R.string.firebase_database_professor_email_key), emailid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String docId = "";
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        docId = document.getId();
                                    }
                                    DocumentReference document = db.collection(getString(R.string.firebase_database_professor_email_key)).document(docId);
                                    document.update(getString(R.string.firebase_database_professor_available_key), true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(rootView, getString(R.string.availability_marked_available), BaseTransientBottomBar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });


        markUnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String emailid = user.getEmail();

                db.collection(getString(R.string.firebase_database_professor_collection))
                        .whereEqualTo(getString(R.string.firebase_database_professor_email_key), emailid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String docId = "";
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        docId = document.getId();
                                    }
                                    DocumentReference document = db.collection(getString(R.string.firebase_database_professor_email_key)).document(docId);
                                    document.update(getString(R.string.firebase_database_professor_available_key), false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(rootView, getString(R.string.availability_marked_unavailable), BaseTransientBottomBar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        return rootView;
    }
}
