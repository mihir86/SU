package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.su.Adapters.LaundryAdapter;
import com.example.su.Items.LaundryOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyLaundryFragment extends Fragment {

	public MyLaundryFragment(){}

	private RecyclerView recyclerView;
	private ConstraintLayout emptyView;
	private ProgressBar progressBar;

	private SwipeRefreshLayout swipeContainer;

    LaundryAdapter adapter;
    private DatabaseReference laundryRef;

	private ArrayList<LaundryOrder> laundryOrders;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_laundry, container, false);

		recyclerView = rootView.findViewById(R.id.my_laundry_recycler_view);
		emptyView = rootView.findViewById(R.id.laundry_empty_view);
		progressBar = rootView.findViewById(R.id.progress_circular_laundry_orders);

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
		laundryRef = FirebaseDatabase.getInstance().getReference().child("Laundry orders"); //TODO: Get room number from email and append .child(RoomNo)) to this line.

		laundryOrders = new ArrayList<>();
		laundryRef.keepSynced(true);

		//TODO: get the data of laundry orders and store it in laundryOrders
		laundryRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
					LaundryOrder laundryOrder = dataSnapshot1.getValue(LaundryOrder.class);
					laundryOrders.add(laundryOrder);
				}

				if (laundryOrders.isEmpty()) {
					setEmptyView();
				} else {
					setRecyclerView();
					adapter = new LaundryAdapter(laundryOrders);
					recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
					recyclerView.setAdapter(adapter);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
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
