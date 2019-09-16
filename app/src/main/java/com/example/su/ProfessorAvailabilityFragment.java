package com.example.su;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.su.Adapters.ProfessorAdapter;
import com.example.su.Items.Professor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ProfessorAvailabilityFragment extends Fragment {
	public ProfessorAvailabilityFragment() {}
	private RecyclerView recyclerView;
	private DatabaseReference profref;
	ArrayList<Professor> professors;
	ProfessorAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_professor_availability, container, false);

		recyclerView = rootView.findViewById(R.id.professor_availability_recycler_view);
		profref = FirebaseDatabase.getInstance().getReference().child("Professors");

		professors = new ArrayList<Professor>();
		//TODO: get professor details and store them in professors and add a progress bar during the network call for professors
		profref.keepSynced(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



		profref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
					Professor professor = dataSnapshot1.getValue(Professor.class);
					professors.add(professor);
				}
				adapter = new ProfessorAdapter(professors);
				recyclerView.setAdapter(adapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
			}
		});



		return rootView;
	}
}
