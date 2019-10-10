package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class UpdateAvailabilityFragment extends Fragment {

	CardView markAvailable;
	CardView markUnavailable;

	public UpdateAvailabilityFragment() {}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_availability_update, container, false);

		markAvailable = rootView.findViewById(R.id.mark_available_card_view);
		markUnavailable = rootView.findViewById(R.id.mark_unavailable_card_view);

		markAvailable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO: Add code to update availability
                Snackbar.make(rootView, getString(R.string.availability_marked_available), BaseTransientBottomBar.LENGTH_LONG).show();
			}
		});


		markUnavailable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO: Add code to update availability
                Snackbar.make(rootView, getString(R.string.availability_marked_unavailable), BaseTransientBottomBar.LENGTH_LONG).show();
			}
		});
		return rootView;
	}
}
