package com.example.su;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Adapters.CabShareAdapter;
import com.example.su.Items.CabShareRequest;

import java.util.ArrayList;
import java.util.Date;

public class CabSharingFragment extends Fragment {

    public CabSharingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cab_sharing, container, false);

        ArrayList<CabShareRequest> userCabRequests = new ArrayList<>();
        userCabRequests.add(new CabShareRequest("2019A7PS1204H", CabShareRequest.CAMPUS_TO_AIRPORT, new Date(), 1.0));
        userCabRequests.add(new CabShareRequest("2019A7PS1204H", CabShareRequest.AIRPORT_TO_CAMPUS, new Date(), 2.0));

        RecyclerView cabSharingRequestsRecycler = rootView.findViewById(R.id.cab_sharing_requests_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        cabSharingRequestsRecycler.setLayoutManager(layoutManager);
        CabShareAdapter cabAdapter = new CabShareAdapter(userCabRequests);
        cabSharingRequestsRecycler.setAdapter(cabAdapter);

        return rootView;
    }

}
