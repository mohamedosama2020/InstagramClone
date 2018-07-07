package tabian.com.instagramclone.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.FirebaseMethods;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;
    String append = "";


    private FirebaseMethods firebaseMethods;
    private Context mContext;
    private String email,username,password;
    private EditText mEmail , mUsername , mPassword;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: Started");
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);

        initWidgets();
        setupFirebaseAuth();
        init();

    }

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if(checkInputs(email,password,username)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    firebaseMethods.registerNewEmail(email,password,username);

                }

            }
        });
    }

    private boolean checkInputs (String email , String username , String password){

        Log.d(TAG, "checkInputs: Checking Inputs For Null Strings");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
            return false;

        }
        else{
            return true;
        }
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull:  Checking if string is null");
        if (string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    private void initWidgets(){
        Log.d(TAG, "initWidgets: Intializing Widgets");
        mEmail = findViewById(R.id.inputEmail);
        mPassword = findViewById(R.id.inputPassword);
        mUsername = findViewById(R.id.inputUsername);
        mProgressBar = findViewById(R.id.registerRequestLoadingProgressBar);
        mProgressBar.setVisibility(View.GONE);
        btnRegister = findViewById(R.id.btn_register);
        mContext  = RegisterActivity.this;

    }




    // ****************************** FIRE BASE ********************************//

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: Setting Up.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        maAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();


                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged: Signed In" + user.getUid());
                    myref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // first check username is not already in use

                            if(firebaseMethods.checkIfUsernameExists(username,dataSnapshot)){
                                append = myref.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: Username Exists! Appending random string to name: " +append );
                            }
                            username = username + append;


                            //add new user to databae


                            //add new user_account_settings to database
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
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
    protected void onStop() {
        super.onStop();
        if(maAuthListener != null)
            mAuth.removeAuthStateListener(maAuthListener);
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
