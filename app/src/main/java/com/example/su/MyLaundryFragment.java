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

import com.example.su.Adapters.LaundryAdapter;
import com.example.su.Items.LaundryOrder;

import java.util.ArrayList;

public class MyLaundryFragment extends Fragment {

	public MyLaundryFragment(){}

	private RecyclerView recyclerView;
	private ConstraintLayout emptyView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_laundry, container, false);

		recyclerView = rootView.findViewById(R.id.my_laundry_recycler_view);
		emptyView = rootView.findViewById(R.id.laundry_empty_view);

		ArrayList<LaundryOrder> laundryOrders = new ArrayList<>();
		//TODO: get the data of laundry orders and store it in laundryOrders and add a progress bar during the network call

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
	}

	private void setEmptyView()
	{
		recyclerView.setVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.VISIBLE);
	}

}
