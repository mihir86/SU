package com.example.su;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerDialogFragment extends BottomSheetDialogFragment {

    public static final int NO_FAB = 1;
    public static final int ADD_FAB = 2;
    String name;
    String email;
    Uri photoUri;
    NavigationDrawerSelected callback;
    TextView nameTextView;
    TextView emailTextView;
    CircleImageView accountPicImageView;
    private boolean isStudent;

    private boolean checkIsStudent() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        String studentOrProfessor = sharedPref.getString(getString(R.string.student_or_prof_key), getString(R.string.student_value));
        return studentOrProfessor.equals(getString(R.string.student_value));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isStudent = checkIsStudent();
        if (isStudent) {
            return inflater.inflate(R.layout.fragment_bottomsheet_for_student, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_bottomsheet_for_prof, container, false);
        }
    }

    private void loadFragment(Fragment fragment, String tag) {
        this.dismiss();
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, fragment, tag)
                    .commit();
        }
    }

    public void setNavigationDrawerSelected(NavigationDrawerSelected callback) {
        this.callback = callback;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emailTextView = getView().findViewById(R.id.myProfileEmailTextView);
        accountPicImageView = getView().findViewById(R.id.myProfileImageView);
        nameTextView = getView().findViewById(R.id.myProfileNameTextView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name = user.getDisplayName();
        email = user.getEmail();
        photoUri = user.getPhotoUrl();
        Log.i("Photo URL", photoUri.toString());

        nameTextView.setText(name);
        emailTextView.setText(email);
        Picasso.get().load(photoUri).resize(64, 64).into(accountPicImageView);

        NavigationView navigationView;
        if (isStudent) {
            navigationView = getView().findViewById(R.id.navigation_view_student);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.my_laundry:
                            loadFragment(new MyLaundryFragment(), "my_laundry");
                            callback.onFragmentSelected(NO_FAB);
                            return true;
                        case R.id.professor_availability:
                            loadFragment(new ProfessorAvailabilityFragment(), "professor_availability");
                            callback.onFragmentSelected(NO_FAB);
                            return true;
                        case R.id.airport_cab_sharing:
                            loadFragment(new CabSharingFragment(), "airport_cab_sharing");
                            callback.onFragmentSelected(ADD_FAB);
                            return true;
                        default:
                            return false;
                    }
                }
            });
        } else {
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
                        default:
                            return false;
                    }
                }
            });
        }

    }

    public interface NavigationDrawerSelected {
        void onFragmentSelected(int fragmentType);
    }
}
