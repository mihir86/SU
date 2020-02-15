package com.example.su;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Adapters.CabShareSimilarRequestAdapter;
import com.example.su.Items.CabShareRequest;
import com.example.su.Items.CabShareSimilarRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CabShareRequestDetailsFragment extends Fragment {

    private CabShareRequest mRequestDetails;
    private ArrayList<CabShareSimilarRequest> mSimilarRequestsDetails;
    private TextView mSharingDetailsHeaderTextView;
    private TextView mSharingDetailsSubHeaderTextView;
    private TextView mSharingDetailsSubSubHeaderTextView;
    private ImageView mEditDetailsImageView;
    private ImageView mDeleteRequestImageView;
    private RecyclerView mSimilarRequestsRecycler;
    private ConstraintLayout mEmptyView;
    private ProgressBar mProgressBar;
    private TextView mMatchesTextView;
    private FirebaseFirestore db;

    public CabShareRequestDetailsFragment(CabShareRequest cabShareRequest) {
        mRequestDetails = cabShareRequest;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_cab_share_request_details, container, false);

        ((MainActivity) getActivity()).fab.hide();

        mSimilarRequestsRecycler = rootView.findViewById(R.id.cab_sharing_details_similar_flights_recycler_view);
        mEmptyView = rootView.findViewById(R.id.cab_sharing_details_empty_view);
        mProgressBar = rootView.findViewById(R.id.progress_circular_cab_requests_details);
        mMatchesTextView = rootView.findViewById(R.id.cab_sharing_details_fragment_matches);

        setLoadingView();

        mEditDetailsImageView = rootView.findViewById(R.id.cab_sharing_details_edit_button);
        mDeleteRequestImageView = rootView.findViewById(R.id.cab_sharing_details_delete_button);

        mEditDetailsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrEditCabSharingBottomDialogFragment cabSharingBottomDialogFragment = new AddOrEditCabSharingBottomDialogFragment(
                        AddOrEditCabSharingBottomDialogFragment.EDIT_CAB_SHARING_REQUEST_MODE, mRequestDetails, getContext());
                cabSharingBottomDialogFragment.show(getFragmentManager(), "edit_cab_sharing_request");
            }
        });

        mDeleteRequestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getString(R.string.delete_request))
                        .setMessage(getString(R.string.delete_request_msg))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRequest();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            }
        });

        db = FirebaseFirestore.getInstance();
        mSimilarRequestsDetails = new ArrayList<>();

        mSharingDetailsHeaderTextView = rootView.findViewById(R.id.cab_sharing_details_fragment_header);

        if (mRequestDetails.getCabType() == CabShareRequest.CAMPUS_TO_AIRPORT) {
            mSharingDetailsHeaderTextView.setText(getString(R.string.campus_to_airport));
        } else {
            mSharingDetailsHeaderTextView.setText(getString(R.string.airport_to_campus));
        }

        mSharingDetailsSubHeaderTextView = rootView.findViewById(R.id.cab_sharing_details_fragment_sub_header);
        mSharingDetailsSubHeaderTextView.setText(formatDate(mRequestDetails.getFlightDateWithTime(), mRequestDetails.getCabType()));

        mSharingDetailsSubSubHeaderTextView = rootView.findViewById(R.id.cab_sharing_details_fragment_flexibility);
        mSharingDetailsSubSubHeaderTextView.setText(formatWaitTime(mRequestDetails.getWaitTime(), mRequestDetails.getCabType()));

        final Date[] requesterDateRange = getDateRange(mRequestDetails);

        db.collection(getString(R.string.firebase_database_cab_sharing_collection))
                .whereEqualTo(getString(R.string.firebase_database_cab_type_field), mRequestDetails.getCabType())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getLong(getString(R.string.firebase_database_requester_phone_field)) != mRequestDetails.getrequesterPhone()) {
                                    CabShareSimilarRequest otherPersonsCabShareRequest = new CabShareSimilarRequest(document.getLong(getString(R.string.firebase_database_requester_phone_field)),
                                            document.getDate(getString(R.string.firebase_database_flight_date_field)),
                                            document.getLong(getString(R.string.firebase_database_wait_time_field))
                                    );
                                    Date[] otherPersonsDateRange = getDateRange(otherPersonsCabShareRequest);

                                    //this if statement checks whether the two date ranges intersect.
                                    if (requesterDateRange[0].before(otherPersonsDateRange[1]) && requesterDateRange[1].after(otherPersonsDateRange[0])) {
                                        mSimilarRequestsDetails.add(otherPersonsCabShareRequest);
                                    }
                                }
                            }

                            if (mSimilarRequestsDetails.isEmpty()) {
                                setEmptyView();
                            } else {
                                setRecyclerView();
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mSimilarRequestsRecycler.setLayoutManager(layoutManager);
                                CabShareSimilarRequestAdapter requestAdapter = new CabShareSimilarRequestAdapter(mSimilarRequestsDetails, mRequestDetails.getCabType(), getContext());
                                mSimilarRequestsRecycler.setAdapter(requestAdapter);
                            }

                        } else {
                            setEmptyView();
                            Log.e("Similar flights", "Error getting documents", task.getException());
                        }
                    }
                });


        return rootView;
    }

    private Date[] getDateRange(CabShareSimilarRequest request) {
        double waitTime = request.getFlexibility();
        int waitTimeInMins = (int) waitTime * 60;
        Date flightDate = request.getDateAndTimeOfFlight();

        Calendar lowerLimitCal = Calendar.getInstance();
        lowerLimitCal.setTime(flightDate);
        lowerLimitCal.add(Calendar.MINUTE, -waitTimeInMins);

        Calendar upperLimitCal = Calendar.getInstance();
        upperLimitCal.setTime(flightDate);
        upperLimitCal.add(Calendar.MINUTE, waitTimeInMins);

        Date[] dates = {lowerLimitCal.getTime(), upperLimitCal.getTime()};
        return dates;
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

    private String formatDate(Date date, long cabType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if (cabType == CabShareRequest.CAMPUS_TO_AIRPORT)
            return getString(R.string.flight_leaves_on) + " " + dateFormat.format(date) + " " + getString(R.string.at) + " " + hourFormat.format(date);
        else
            return getString(R.string.flight_arrives_on) + " " + dateFormat.format(date) + " " + getString(R.string.at) + " " + hourFormat.format(date);
    }

    private String formatWaitTime(double waitTime, long cabType) {
        if (cabType == CabShareRequest.CAMPUS_TO_AIRPORT)
            return getString(R.string.will_leave) + " " + waitTime + " " + getString(R.string.hrs) + " " + getString(R.string.early);
        else
            return getString(R.string.will_leave) + " " + waitTime + " " + getString(R.string.hrs) + " " + getString(R.string.late);
    }

    private void setRecyclerView() {
        mSimilarRequestsRecycler.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mMatchesTextView.setVisibility(View.VISIBLE);
    }

    private void setEmptyView() {
        mSimilarRequestsRecycler.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mMatchesTextView.setVisibility(View.INVISIBLE);
    }

    private void setLoadingView() {
        mSimilarRequestsRecycler.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mMatchesTextView.setVisibility(View.INVISIBLE);
    }

    private void deleteRequest() {
        db.collection(getString(R.string.firebase_database_cab_sharing_collection)).document(mRequestDetails.getDocumentID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), getString(R.string.deleted_request), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
