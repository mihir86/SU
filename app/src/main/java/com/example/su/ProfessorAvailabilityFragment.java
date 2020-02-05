package com.example.su;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.su.Adapters.ProfessorAdapter;
import com.example.su.Items.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfessorAvailabilityFragment extends Fragment {

	public ProfessorAvailabilityFragment() {}

    private SwipeRefreshLayout swipeContainer;

	private RecyclerView recyclerView;
	private ProgressBar progressBar;
    private FirebaseFirestore db;
	ArrayList<Professor> professors;
	ProfessorAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_professor_availability, container, false);

		recyclerView = rootView.findViewById(R.id.professor_availability_recycler_view);
		progressBar = rootView.findViewById(R.id.progress_circular_professor_availability);

        db = FirebaseFirestore.getInstance();

        setLoadingView();
        setupRecyclerData();

        swipeContainer = rootView.findViewById(R.id.professor_swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
            public void onRefresh() {
                adapter.clear();
                setupRecyclerData();
                swipeContainer.setRefreshing(false);
			}
		});

		return rootView;
	}

    private void setupRecyclerData() {
        professors = new ArrayList<>();

        db.collection(getString(R.string.firebase_database_professor_collection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                professors.add(new Professor(
                                        document.getString(getString(R.string.firebase_database_professor_email_key)),
                                        document.getString(getString(R.string.firebase_database_professor_name_key)),
                                        document.getLong(getString(R.string.firebase_database_professor_dept_key)),
                                        document.getString(getString(R.string.firebase_database_professor_roomNo_key)),
                                        document.getBoolean(getString(R.string.firebase_database_professor_available_key))
                                ));
                            }
                            setRecyclerView();
                            adapter = new ProfessorAdapter(professors);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }

	private void setLoadingView()
	{
		recyclerView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void setRecyclerView()
	{
		recyclerView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
	}

}
