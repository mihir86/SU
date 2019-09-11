package com.example.su;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NavigationDrawerDialogFragment extends BottomSheetDialogFragment {


	private NavigationView navigationView;
	public static String tag;


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bottomsheet, container, false);
	}

	private void loadFragment(Fragment fragment, String tag)
	{
		this.dismiss();
		if (fragment != null){
		getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_frame, fragment, tag)
				.commit();
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		navigationView = getView().findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.my_laundry:
						loadFragment(new MyLaundryFragment(), "my_laundry");
						return true;
					case R.id.professor_availability:
						loadFragment(new ProfessorAvailability(), "professor_availability");
						return true;
					case R.id.my_profile:
						loadFragment(new MyProfileFragment(), "my_profile");
					default:
						return false;
				}
			}
		});
	}
}
