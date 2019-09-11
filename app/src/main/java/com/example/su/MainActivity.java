package com.example.su;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {
	BottomAppBar navBar;
	NavigationDrawerDialogFragment bottomNavigationDrawerFragment;

	public static final String BOTTOM_NAV_MENU_TAG = "bottom_nav_menu";


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
				//TODO: sign the user out from the app
				Intent intent = new Intent(MainActivity.this, SignInActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
