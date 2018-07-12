package tabian.com.instagramclone.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import tabian.com.instagramclone.Login.LoginActivity;
import tabian.com.instagramclone.R;

public class SignOutFragment extends Fragment {
    private static final String TAG = "SignOutFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private ProgressBar mProgressBar;
    private Button btnSignout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        mProgressBar = view.findViewById(R.id.progressbarSignOut);
        btnSignout = view.findViewById(R.id.btnConfirmSignout);
        mProgressBar.setVisibility(View.GONE);

        setupFirebaseAuth();



        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attemping to sign out");
                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.signOut();
                getActivity().finish();
            }
        });

        return view;
    }

    // ****************************** FIRE BASE ********************************//

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: Setting Up.");
        mAuth = FirebaseAuth.getInstance();
        maAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged: Signed In" + user.getUid());
                }
                else {
                    Log.d(TAG, "onAuthStateChanged: Signed Out");

                    Log.d(TAG, "onAuthStateChanged: Navigating Back To Login Screen");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(maAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(maAuthListener != null)
            mAuth.removeAuthStateListener(maAuthListener);
    }
}
