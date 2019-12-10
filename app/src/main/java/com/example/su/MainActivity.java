package com.example.su;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements NavigationDrawerDialogFragment.NavigationDrawerSelected {

	BottomAppBar navBar;
    public static final String BOTTOM_ADD_CAB_SHARING_REUQEST_TAG = "bottom_add_cab_sharing_request";
    FloatingActionButton fab;

	NavigationDrawerDialogFragment bottomNavigationDrawerFragment;


	public static final String BOTTOM_NAV_MENU_TAG = "bottom_nav_menu";
    private Context mContext;

	GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onFragmentSelected(int fragmentType) {
        if (fragmentType == NavigationDrawerDialogFragment.NO_FAB)
            fab.hide();
        else
            fab.show();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof NavigationDrawerDialogFragment) {
            NavigationDrawerDialogFragment drawerDialogFragment = (NavigationDrawerDialogFragment) fragment;
            drawerDialogFragment.setNavigationDrawerSelected(this);
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(isStudent())
		{
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_frame, new MyLaundryFragment(), "my_laundry")
					.commit();
		}
		else{
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_frame, new UpdateAvailabilityFragment(), "update_availability")
					.commit();
		}

        mContext = getApplicationContext();

        fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Display bottom sheet here
                AddCabSharingBottomDialogFragment cabSharingBottomDialogFragment = new AddCabSharingBottomDialogFragment();
                cabSharingBottomDialogFragment.show(getSupportFragmentManager(), BOTTOM_ADD_CAB_SHARING_REUQEST_TAG);
            }
        });

		navBar = findViewById(R.id.bottom_nav_bar);
		setSupportActionBar(navBar);

		bottomNavigationDrawerFragment = new NavigationDrawerDialogFragment();

		navBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bottomNavigationDrawerFragment.show(getSupportFragmentManager(), BOTTOM_NAV_MENU_TAG);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.nav_bar_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		switch (item.getItemId()) {
			case R.id.sign_out:
				GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
						.requestIdToken(getString(R.string.auth_id))
						.requestEmail()
						.build();
				mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

				mGoogleSignInClient.signOut()
						.addOnCompleteListener(this, new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								Intent intent = new Intent(MainActivity.this, SignInActivity.class);
								startActivity(intent);
							}
						});



				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean isStudent()
	{
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
		String studentOrProfessor = sharedPref.getString(getString(R.string.student_or_prof_key), getString(R.string.student_value));

		return studentOrProfessor.equals(getString(R.string.student_value));
	}
}
