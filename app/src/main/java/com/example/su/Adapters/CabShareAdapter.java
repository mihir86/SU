package com.example.su.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.CabShareRequestDetailsFragment;
import com.example.su.Items.CabShareRequest;
import com.example.su.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CabShareAdapter extends RecyclerView.Adapter<CabShareAdapter.MyViewHolder> {

    private ArrayList<CabShareRequest> mDataset;

    private FirebaseFirestore db;

    private Context mContext;
    private FragmentManager mFragmentManager;

    public CabShareAdapter(ArrayList<CabShareRequest> cabShareRequests, Context context) {
        mDataset = cabShareRequests;
        db = FirebaseFirestore.getInstance();
        mContext = context;
        mFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cabShareView = inflater.inflate(R.layout.cab_share_list_item, parent, false);
        return new MyViewHolder(cabShareView);
    }

    private void openDetailsFragment(int position) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, new CabShareRequestDetailsFragment(mDataset.get(position)), "cab_sharing_request_details")
                .commit();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final CabShareRequest cabRequest = mDataset.get(position);

        TextView cabShareTitleTextView = holder.cabShareTitleTextView;
        TextView cabDateTimeTextView = holder.cabDateTimeTextView;
        TextView cabFlexibilityTextView = holder.cabFlexibilityTextView;
        final TextView matchesTextView = holder.matchesTextView;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsFragment(position);
            }
        });

        String cabFlexibilityText;
        if (cabRequest.getCabType() == CabShareRequest.CAMPUS_TO_AIRPORT) {
            cabShareTitleTextView.setText(mContext.getString(R.string.campus_to_airport));
            cabFlexibilityText = mContext.getString(R.string.plus) + cabRequest.getWaitTime() + mContext.getString(R.string.hrs);
        } else {
            cabShareTitleTextView.setText(mContext.getString(R.string.airport_to_campus));
            cabFlexibilityText = mContext.getString(R.string.minus) + cabRequest.getWaitTime() + mContext.getString(R.string.hrs);
        }
        cabFlexibilityTextView.setText(cabFlexibilityText);

        final Date[] requesterDateRange = getDateRange(cabRequest);

        db.collection(mContext.getString(R.string.firebase_database_cab_sharing_collection))
                .whereEqualTo(mContext.getString(R.string.firebase_database_cab_type_field), cabRequest.getCabType())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int matches = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getLong(mContext.getString(R.string.firebase_database_requester_phone_field)) != cabRequest.getrequesterPhone()) {
                                    CabShareRequest otherPersonsCabShareRequest = new CabShareRequest(document.getId(), document.getLong(mContext.getString(R.string.firebase_database_requester_phone_field)),
                                            document.getLong(mContext.getString(R.string.firebase_database_cab_type_field)),
                                            document.getDate(mContext.getString(R.string.firebase_database_flight_date_field)),
                                            document.getLong(mContext.getString(R.string.firebase_database_wait_time_field))
                                    );
                                    Date[] otherPersonsDateRange = getDateRange(otherPersonsCabShareRequest);

                                    //this if statement checks whether the two date ranges intersect.
                                    if (requesterDateRange[0].before(otherPersonsDateRange[1]) && requesterDateRange[1].after(otherPersonsDateRange[0])) {
                                        matches++;
                                    }
                                }
                            }
                            String matchesText;
                            if (matches == 0)
                                matchesText = mContext.getString(R.string.no_matches);
                            else if (matches == 1)
                                matchesText = mContext.getString(R.string.one_match_found);
                            else
                                matchesText = matches + " " + mContext.getString(R.string.matches_found);
                            matchesTextView.setText(matchesText);
                        } else {
                            Log.e("Similar flights", "Error getting documents", task.getException());
                        }
                    }
                });

        cabDateTimeTextView.setText(formattedDate(cabRequest.getFlightDateWithTime()));
    }

    private Date[] getDateRange(CabShareRequest request) {
        double waitTime = request.getWaitTime();
        int waitTimeInMins = (int) waitTime * 60;
        Date flightDate = request.getFlightDateWithTime();

        Calendar lowerLimitCal = Calendar.getInstance();
        lowerLimitCal.setTime(flightDate);
        lowerLimitCal.add(Calendar.MINUTE, -waitTimeInMins);

        Calendar upperLimitCal = Calendar.getInstance();
        upperLimitCal.setTime(flightDate);
        upperLimitCal.add(Calendar.MINUTE, waitTimeInMins);

        Date[] dates = {lowerLimitCal.getTime(), upperLimitCal.getTime()};
        return dates;
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
