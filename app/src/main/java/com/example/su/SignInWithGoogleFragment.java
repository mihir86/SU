package com.example.su;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.content.Context.MODE_PRIVATE;

public class SignInWithGoogleFragment extends Fragment {

    private final static int RC_SIGN_IN = 2;
    SignInButton googleSignInButton;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    View rootView;

    public SignInWithGoogleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_in_with_google, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        googleSignInButton = rootView.findViewById(R.id.google_sign_in_button);
        googleSignInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return rootView;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                if (checkIfBITSEmailAndStoreDetails(account.getEmail())) {
                    Navigation.findNavController(rootView).navigate(R.id.action_signInWithGoogleFragment_to_introFragment);
                } else {
                    mGoogleSignInClient.revokeAccess();
                    Snackbar.make(rootView.findViewById(R.id.sign_in_layout), getString(R.string.sign_in_use_bits_mail), Snackbar.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
                Log.d("SignInDebug", "message - " + e.getStatusCode());
                Snackbar.make(rootView.findViewById(R.id.sign_in_layout), getString(R.string.sign_in_failed), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Snackbar.make(rootView.findViewById(R.id.sign_in_layout), getString(R.string.sign_in_use_bits_mail), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkIfBITSEmailAndStoreDetails(String email) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (email.toLowerCase().endsWith("@hyderabad.bits-pilani.ac.in")) {
            if (email.toLowerCase().startsWith("f20")) {
                editor.putString(getString(R.string.student_or_prof_key), getString(R.string.student_value));
                editor.apply();
            } else {
                editor.putString(getString(R.string.student_or_prof_key), getString(R.string.prof_value));
                editor.apply();
            }
            return true;
        } else if (email.equalsIgnoreCase("aryan.arora180@gmail.com") || email.equalsIgnoreCase("mstrmihir2@gmail.com") || email.equalsIgnoreCase("k.vineeth2000@gmail.com") || email.equalsIgnoreCase("abuch99@gmail.com")) {
            editor.putString(getString(R.string.student_or_prof_key), getString(R.string.prof_value));
            editor.apply();
            return true;
        } else
            return false;
    }

}
