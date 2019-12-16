package com.example.su.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Items.CabShareRequest;
import com.example.su.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CabShareAdapter extends RecyclerView.Adapter<CabShareAdapter.MyViewHolder> {

    private ArrayList<CabShareRequest> mDataset;

    public CabShareAdapter(ArrayList<CabShareRequest> cabShareRequests) {
        mDataset = cabShareRequests;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cabShareView = inflater.inflate(R.layout.cab_share_list_item, parent, false);
        return new MyViewHolder(cabShareView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CabShareRequest cabRequest = mDataset.get(position);

        TextView cabShareTitleTextView = holder.cabShareTitleTextView;
        TextView cabDateTimeTextView = holder.cabDateTimeTextView;
        TextView cabFlexibilityTextView = holder.cabFlexibilityTextView;
        TextView matchesTextView = holder.matchesTextView;

        if (cabRequest.getCabType() == CabShareRequest.CAMPUS_TO_AIRPORT) {
            cabShareTitleTextView.setText("Campus to airport");
            cabFlexibilityTextView.setText("- " + cabRequest.getWaitTime() + " hrs");
        } else {
            cabShareTitleTextView.setText("Airport to campus");
            cabFlexibilityTextView.setText("+ " + cabRequest.getWaitTime() + " hrs");
            matchesTextView.setText("1 match found");
        }

        cabDateTimeTextView.setText(formattedDate(cabRequest.getFlightDateWithTime()));

        String idNumber = cabRequest.getRequesterID();

        //TODO: Search Firebase for similarly timed requests and display them in noMatchesTextView

    }

    private String formattedDate(Date flightDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, MMM d", Locale.getDefault());
        return dateFormat.format(flightDate);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cabShareTitleTextView;
        TextView cabDateTimeTextView;
        TextView cabFlexibilityTextView;
        TextView noMatchesTextView;
        TextView matchesTextView;

        public MyViewHolder(View itemView) {

            super(itemView);

            cabShareTitleTextView = itemView.findViewById(R.id.cab_share_title_text_view);
            cabDateTimeTextView = itemView.findViewById(R.id.cab_date_time_text_view);
            cabFlexibilityTextView = itemView.findViewById(R.id.cab_flexibility_text_view);
            matchesTextView = itemView.findViewById(R.id.matches_found_text_view);
        }
    }
}
