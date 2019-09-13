package com.example.su;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.su.Adapters.ProfessorAdapter;
import com.example.su.Items.Professor;

import java.util.ArrayList;

public class ProfessorAvailabilityFragment extends Fragment {


	public ProfessorAvailabilityFragment() {}

	private RecyclerView recyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_professor_availability, container, false);

		recyclerView = rootView.findViewById(R.id.professor_availability_recycler_view);

		ArrayList<Professor> professors = new ArrayList<>();
		//TODO: get professor details and store them in professors and add a progress bar during the network call for professors

		ProfessorAdapter adapter = new ProfessorAdapter(professors);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);

		return rootView;
	}

}
