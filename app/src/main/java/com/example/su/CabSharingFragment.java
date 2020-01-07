package com.example.su;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Adapters.CabShareAdapter;
import com.example.su.Items.CabShareRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CabSharingFragment extends Fragment {

    ArrayList<CabShareRequest> mUserCabRequests;
    CabShareAdapter mCabAdapter;
    private FirebaseFirestore db;
    private RecyclerView mCabSharingRequestsRecycler;
    private ConstraintLayout mEmptyView;
    private ProgressBar mProgressBar;
    private TextView mYourRequestsTextView;

    public CabSharingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cab_sharing, container, false);

        mUserCabRequests = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        mCabSharingRequestsRecycler = rootView.findViewById(R.id.cab_sharing_requests_recycler_view);
        mEmptyView = rootView.findViewById(R.id.cab_sharing_empty_view);
        mProgressBar = rootView.findViewById(R.id.progress_circular_cab_requests);
        mYourRequestsTextView = rootView.findViewById(R.id.your_cab_sharing_requests);

        setLoadingView();

        db.collection(getString(R.string.firebase_database_cab_sharing_collection))
                .whereEqualTo(getString(R.string.firebase_database_requester_phone_field), 8007187941L) //TODO: Get user phone number and pass that instead
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mUserCabRequests.add(new CabShareRequest(document.getId(), document.getLong(getString(R.string.firebase_database_requester_phone_field)),
                                        document.getLong(getString(R.string.firebase_database_cab_type_field)),
                                        document.getDate(getString(R.string.firebase_database_flight_date_field)),
                                        document.getDouble(getString(R.string.firebase_database_wait_time_field))));
                                mCabAdapter.notifyDataSetChanged();
                            }
                            if (mUserCabRequests.isEmpty())
                                setEmptyView();
                            else
                                setRecyclerView();
                        } else {
                            Log.d("CabSharingFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mCabSharingRequestsRecycler.setLayoutManager(layoutManager);
        mCabAdapter = new CabShareAdapter(mUserCabRequests, getContext());
        mCabSharingRequestsRecycler.setAdapter(mCabAdapter);

        return rootView;
    }

    private void setRecyclerView() {
        mCabSharingRequestsRecycler.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mYourRequestsTextView.setVisibility(View.VISIBLE);
    }

    private void setEmptyView() {
        mCabSharingRequestsRecycler.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mYourRequestsTextView.setVisibility(View.INVISIBLE);
    }

    private void setLoadingView() {
        mCabSharingRequestsRecycler.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mYourRequestsTextView.setVisibility(View.INVISIBLE);
    }

}
