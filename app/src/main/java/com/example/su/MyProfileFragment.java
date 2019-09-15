package com.example.su;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyProfileFragment extends Fragment {


	public MyProfileFragment() {}

	TextView emailTextView;
	ImageView accountPicImageView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

		emailTextView = rootView.findViewById(R.id.profileEmailTextView);
		accountPicImageView = rootView.findViewById(R.id.profileImageView);

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		String name = user.getDisplayName();
		String email = user.getEmail();
		Uri photoUrl = user.getPhotoUrl();

		emailTextView.setText(email);

		return rootView;
	}

}
