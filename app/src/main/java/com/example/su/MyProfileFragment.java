package com.example.su;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MyProfileFragment extends Fragment {


	public MyProfileFragment() {}

    TextView nameTextView;
	TextView emailTextView;
	ImageView accountPicImageView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        emailTextView = rootView.findViewById(R.id.myProfileEmailTextView);
        accountPicImageView = rootView.findViewById(R.id.myProfileImageView);
        nameTextView = rootView.findViewById(R.id.myProfileNameTextView);

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		String name = user.getDisplayName();
		String email = user.getEmail();
        Uri photoUri = user.getPhotoUrl();
        Log.i("Photo URL", photoUri.toString());

        nameTextView.setText(name);
		emailTextView.setText(email);
        Picasso.get().load(photoUri).resize(64, 64).into(accountPicImageView);
		return rootView;
	}

}
