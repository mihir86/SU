package com.example.su.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Items.LaundryOrder;
import com.example.su.R;

import java.util.ArrayList;

public class LaundryAdapter extends RecyclerView.Adapter<LaundryAdapter.MyViewHolder> {

	private ArrayList<LaundryOrder> mDataset;

	public LaundryAdapter(ArrayList<LaundryOrder> laundryOrders)
	{
		mDataset = laundryOrders;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView givenDateTextView;
		public TextView priceTextView;
		public ImageView doneImageView;
		public MyViewHolder(View itemView) {
			super(itemView);

			givenDateTextView = itemView.findViewById(R.id.laundry_list_given_date);
			priceTextView = itemView.findViewById(R.id.laundry_list_price);
			doneImageView = itemView.findViewById(R.id.laundry_status_image_view);
		}
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View laundryView = inflater.inflate(R.layout.laundry_list_item, parent, false);
		return new MyViewHolder(laundryView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		LaundryOrder laundryOrder = mDataset.get(position);

		TextView givenDateTextView = holder.givenDateTextView;
		TextView priceTextView = holder.priceTextView;
		ImageView doneImageView = holder.doneImageView;
		String priceToDisplay = "₹" + laundryOrder.getAmount();
		String givenDateToDisplay = "Given on " + laundryOrder.getGivenDate();
		givenDateTextView.setText(givenDateToDisplay);
		priceTextView.setText(priceToDisplay);

		if (laundryOrder.isDone()) {
			Log.e("Reached isDone value:", "true");
			doneImageView.setImageResource(R.drawable.round_done_24);
		} else {
			Log.e("Reached isDone value:", "false");
			doneImageView.setImageResource(R.drawable.not_done_24);
		}
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	public void clear() {
		mDataset.clear();
		notifyDataSetChanged();
	}
}
