package com.example.su;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerDialogFragment extends BottomSheetDialogFragment {


	private boolean isStudent;


	private boolean checkIsStudent()
	{

		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
		String studentOrProfessor = sharedPref.getString(getString(R.string.student_or_prof_key), getString(R.string.student_value));
		if(studentOrProfessor.equals(getString(R.string.student_value)))
			return true;
		else
			return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		isStudent = checkIsStudent();
		if(isStudent)
		{
			return inflater.inflate(R.layout.fragment_bottomsheet_for_student, container, false);
		}
		else
		{
			return inflater.inflate(R.layout.fragment_bottomsheet_for_prof, container, false);
		}
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

		NavigationView navigationView;
		if(isStudent)
		{
			navigationView = getView().findViewById(R.id.navigation_view_student);
			navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.my_laundry:
							loadFragment(new MyLaundryFragment(), "my_laundry");
							return true;
						case R.id.professor_availability:
							loadFragment(new ProfessorAvailabilityFragment(), "professor_availability");
							return true;
						case R.id.my_profile:
							loadFragment(new MyProfileFragment(), "my_profile");
						case R.id.airport_cab_sharing:
							loadFragment(new CabSharingFragment(), "airport_cab_sharing");
						default:
							return false;
					}
				}
			});
		}
		else
		{
			navigationView = getView().findViewById(R.id.navigation_view_prof);
			navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.my_laundry:
							loadFragment(new MyLaundryFragment(), "my_laundry");
							return true;
						case R.id.update_prof_availability:
							loadFragment(new UpdateAvailabilityFragment(), "update_availability");
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
}
