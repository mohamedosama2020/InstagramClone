package tabian.com.instagramclone.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import tabian.com.instagramclone.Home.HomeActivity;
import tabian.com.instagramclone.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private EditText mEmail,mPassword;



    private static final String TAG = "LoginActivity";
    private ProgressBar progressBar;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Started");

        progressBar = findViewById(R.id.loginRequestLoadingProgressBar);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mContext = LoginActivity.this;

        progressBar.setVisibility(View.GONE);

        setupFirebaseAuth();
        init();


    }

    private void init(){

        //Initialize the button for loggin in
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attemping to log in");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(email.equals("") && password.equals(""))
                {
                    Toast.makeText(mContext, "You Must Enter Email And Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        try{
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if(user.isEmailVerified()){
                                                Log.d(TAG, "onComplete: success email is verified");
                                                Toast.makeText(LoginActivity.this, "Login Successful.",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(mContext, "Email Is Not Verified \n Check Your email inbox", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }

                                        }
                                        catch (NullPointerException e){
                                            Toast.makeText(mContext, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);


                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to register activty");
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });
        /*
        If The User Is Logged In Then Navigate to Home Activity
         */
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
            startActivity(intent);
            finish();

        }
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
}

