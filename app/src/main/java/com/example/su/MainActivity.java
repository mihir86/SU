package com.example.su;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {
	BottomAppBar navBar;
	NavigationDrawerDialogFragment bottomNavigationDrawerFragment;

	public static final String BOTTOM_NAV_MENU_TAG = "bottom_nav_menu";

	GoogleSignInClient mGoogleSignInClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_frame, new MyLaundryFragment(), "my_laundry")
				.commit();

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
}
