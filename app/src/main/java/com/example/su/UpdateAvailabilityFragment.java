package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdateAvailabilityFragment extends Fragment {

	CardView markAvailable;
	CardView markUnavailable;

	FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
	String emailid = user.getEmail();

	DatabaseReference updateref = FirebaseDatabase.getInstance().getReference().child("Professors");

	Query emailquery = updateref.orderByChild("Email").equalTo(emailid);


	public UpdateAvailabilityFragment() {}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_availability_update, container, false);

		markAvailable = rootView.findViewById(R.id.mark_available_card_view);
		markUnavailable = rootView.findViewById(R.id.mark_unavailable_card_view);

		markAvailable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO: Add code to update availability
				emailquery.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						for(DataSnapshot child : dataSnapshot.getChildren()){
							child.getRef().child("Availability").setValue("true");
						}
						Snackbar.make(rootView, getString(R.string.availability_marked_available), BaseTransientBottomBar.LENGTH_LONG).show();
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
					}
				});

			}
		});


		markUnavailable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO: Add code to update availability
				emailquery.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						for(DataSnapshot child : dataSnapshot.getChildren()){
							child.getRef().child("Availability").setValue("false");
						}
						Snackbar.make(rootView, getString(R.string.availability_marked_unavailable), BaseTransientBottomBar.LENGTH_LONG).show();
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		return rootView;
	}
}
