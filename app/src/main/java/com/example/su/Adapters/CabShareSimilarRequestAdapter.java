package com.example.su.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Items.CabShareRequest;
import com.example.su.Items.CabShareSimilarRequest;
import com.example.su.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CabShareSimilarRequestAdapter extends RecyclerView.Adapter<CabShareSimilarRequestAdapter.MyViewHolder> {

    private ArrayList<CabShareSimilarRequest> mDataset;

    private long mCabType;

    private Context mContext;

    public CabShareSimilarRequestAdapter(ArrayList<CabShareSimilarRequest> cabShareSimilarRequests, long cabType, Context context) {
        mDataset = cabShareSimilarRequests;
        mCabType = cabType;
        mContext = context;
    }

    @NonNull
    @Override
    public CabShareSimilarRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cabShareView = inflater.inflate(R.layout.cab_share_similar_flight_list_view, parent, false);
        return new MyViewHolder(cabShareView);
    }

    @Override
    public void onBindViewHolder(@NonNull CabShareSimilarRequestAdapter.MyViewHolder holder, int position) {
        CabShareSimilarRequest cabRequest = mDataset.get(position);

        TextView cabShareNameTextView = holder.cabShareNameTextView;
        TextView cabDateTimeTextView = holder.cabDateTimeTextView;
        TextView cabFlexibilityTextView = holder.cabFlexibilityTextView;
        TextView cabSharePhoneTextView = holder.cabSharePhoneTextView;

        cabShareNameTextView.setText("Placeholder name"); //TODO: Get name from phone number and set it here
        cabDateTimeTextView.setText(formatDate(cabRequest.getDateAndTimeOfFlight()));
        cabFlexibilityTextView.setText(formatWaitTime(cabRequest.getFlexibility()));
        cabSharePhoneTextView.setText(Long.toString(cabRequest.getPhoneNumber()));

    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if (mCabType == CabShareRequest.CAMPUS_TO_AIRPORT)
            return mContext.getString(R.string.flight_leaves_on) + " " + dateFormat.format(date) + " " + mContext.getString(R.string.at) + " " + hourFormat.format(date);
        else
            return mContext.getString(R.string.flight_arrives_on) + " " + dateFormat.format(date) + " " + mContext.getString(R.string.at) + " " + hourFormat.format(date);
    }

    private String formatWaitTime(double waitTime) {
        if (mCabType == CabShareRequest.CAMPUS_TO_AIRPORT)
            return mContext.getString(R.string.will_leave) + " " + waitTime + " " + mContext.getString(R.string.hrs) + " " + mContext.getString(R.string.early);
        else
            return mContext.getString(R.string.will_leave) + " " + waitTime + " " + mContext.getString(R.string.hrs) + " " + mContext.getString(R.string.late);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cabShareNameTextView;
        TextView cabDateTimeTextView;
        TextView cabFlexibilityTextView;
        TextView cabSharePhoneTextView;

        public MyViewHolder(View itemView) {

            super(itemView);

            cabShareNameTextView = itemView.findViewById(R.id.cab_share_similar_flight_name_text_view);
            cabDateTimeTextView = itemView.findViewById(R.id.cab_share_similar_flight_time_text_view);
            cabFlexibilityTextView = itemView.findViewById(R.id.cab_share_similar_flight_flexibility_text_view);
            cabSharePhoneTextView = itemView.findViewById(R.id.cab_share_similar_flight_number_text_view);
        }
    }
}
