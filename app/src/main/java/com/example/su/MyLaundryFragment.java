package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.su.Adapters.LaundryAdapter;
import com.example.su.Items.LaundryOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyLaundryFragment extends Fragment {

	public MyLaundryFragment(){}

	private RecyclerView recyclerView;
	private ConstraintLayout emptyView;
	private ProgressBar progressBar;

	private SwipeRefreshLayout swipeContainer;

    LaundryAdapter adapter;
	private FirebaseFirestore db;

	private ArrayList<LaundryOrder> laundryOrders;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_laundry, container, false);

		recyclerView = rootView.findViewById(R.id.my_laundry_recycler_view);
		emptyView = rootView.findViewById(R.id.laundry_empty_view);
		progressBar = rootView.findViewById(R.id.progress_circular_laundry_orders);

		db = FirebaseFirestore.getInstance();

		setLoadingView();
		setupRecyclerData();

		swipeContainer = rootView.findViewById(R.id.laundry_swipe_container);
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

		laundryOrders = new ArrayList<>();

		db.collection(getString(R.string.firebase_database_laundry_collection))
				.whereEqualTo(getString(R.string.firebase_database_laundry_room_no_field), "VM214L")//TODO: get user room number and pass that instead
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								laundryOrders.add(new LaundryOrder(document.getDate(getString(R.string.firebase_database_laundry_given_date_field)), document.getDouble(getString(R.string.firebase_database_laundry_price_field)), document.getBoolean(getString(R.string.firebase_database_laundry_complete_field))));
							}
							if (laundryOrders.isEmpty()) {
								setEmptyView();
							} else {
								setRecyclerView();
								adapter = new LaundryAdapter(laundryOrders);
								recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
								recyclerView.setAdapter(adapter);
							}
						} else {
							//FAILED
						}
					}
				});
	}

	private void setRecyclerView()
	{
		recyclerView.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void setEmptyView()
	{
		recyclerView.setVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void setLoadingView()
	{
		recyclerView.setVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}

}
