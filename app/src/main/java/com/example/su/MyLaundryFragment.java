package com.example.su;

import android.content.Context;
import android.net.Uri;
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
import android.widget.ProgressBar;

import com.example.su.Adapters.LaundryAdapter;
import com.example.su.Items.LaundryOrder;

import java.util.ArrayList;

public class MyLaundryFragment extends Fragment {

	public MyLaundryFragment(){}

	private RecyclerView recyclerView;
	private ConstraintLayout emptyView;
	private ProgressBar progressBar;

	private ArrayList<LaundryOrder> laundryOrders;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_laundry, container, false);

		recyclerView = rootView.findViewById(R.id.my_laundry_recycler_view);
		emptyView = rootView.findViewById(R.id.laundry_empty_view);
		progressBar = rootView.findViewById(R.id.progress_circular_laundry_orders);

		setLoadingView();

		laundryOrders = new ArrayList<>();
		laundryOrders.add(new LaundryOrder("Sept 16, 2019", 45, false));
		laundryOrders.add(new LaundryOrder("Sept 14, 2019", 65, true));
		//TODO: get the data of laundry orders and store it in laundryOrders

		if(laundryOrders.isEmpty())
		{
			setEmptyView();
		}
		else
		{
			setRecyclerView();
			LaundryAdapter adapter = new LaundryAdapter(laundryOrders);
			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setAdapter(adapter);
		}

		return rootView;
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
