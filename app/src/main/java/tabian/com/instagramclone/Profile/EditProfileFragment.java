package tabian.com.instagramclone.Profile;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import tabian.com.instagramclone.Dialogs.ConfirmPasswordDialog;
import tabian.com.instagramclone.Models.User;
import tabian.com.instagramclone.Models.UserAccountSettings;
import tabian.com.instagramclone.Models.UserSettings;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Share.ShareActivity;
import tabian.com.instagramclone.Utils.FirebaseMethods;

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPassowrdListener {
    private static final String TAG = "EditProfileFragment";

    @Override
    public void onConfirmPassword(String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Re-Authenticated ^_^");



                    ////////////////////////////// Check to see if the email is not already present in database
                    mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                            if(task.isSuccessful()){
                                try{
                                    if(task.getResult().getProviders().size() == 1){
                                        Log.d(TAG, "onComplete: That Email is Already In Use");
                                        Toast.makeText(getActivity(), "That Email is Already In Use", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.d(TAG, "onComplete: That Email is Available");

                                        //////////////////////// the email is available so update it
                                        user.updateEmail(mEmail.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Updated Email Succeccfully", Toast.LENGTH_SHORT).show();
                                                    mFirebaseMethods.updateEmail(mEmail.getText().toString());

                                                }

                                            }
                                        });
                                    }
                                }
                                catch(NullPointerException e){
                                    Log.d(TAG, "onComplete: NullPointerException" + e.getMessage());
                                }
                            }
                        }
                    });





                }else{
                    Log.d(TAG, "onComplete: Authentication Faild :(");
                }
            }
        });

    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;
    private  UserSettings mUserSettings;

    // EditProfile Fragment Widgets
    private EditText mDisplayName , mUsername , mWebsite , mDescription , mEmail , mPhoneNumber;
    private TextView mChangeProfilePhoto;

    private CircleImageView mprofileImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mprofileImageView = view.findViewById(R.id.profile_photo);
        mDisplayName = view.findViewById(R.id.display_name);
        mUsername =  view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mDescription = view.findViewById(R.id.description);
        mEmail = view.findViewById(R.id.email);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);

        mFirebaseMethods = new FirebaseMethods(getActivity());
        setupFirebaseAuth();

        ImageView checkMark = view.findViewById(R.id.saveChanges);
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attemping to save changes ");
                saveProfileSettings();
            }
        });


        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    /**
     * Retrieves the data in the widgets and sends them to database
     * before doing that it checks to make the username chosen is unique
     */
    private void saveProfileSettings(){

        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());


            //case 1: if the user made a change to username

            if(!mUserSettings.getUser().getUsername().equals(username)){

                checkIfUserNameExist(username);
            }
            //case 2: if the user made changes to their email
            if(!mUserSettings.getUser().getEmail().equals(email)){

                //step 1 : Re-Authinticate
                //          -Confirm the password and email
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
                dialog.setTargetFragment(EditProfileFragment.this,1);



                //step 2 :  check if the email is already registerd
                //           -FetchProvidersForEmail(String email)
                //step 3 : change the email
                //          -Submit the new email to the database and authentication


            }

            // Change the rest of settings that don't require  uniqueness
            if(!mUserSettings.getSettings().getDisplay_name().equals(displayName)){
                //Update Display Name
                mFirebaseMethods.updateUserAcccountSettings(displayName,null,null);

            }
            if(!mUserSettings.getSettings().getWebsite().equals(website)){
                //Update Website
                mFirebaseMethods.updateUserAcccountSettings(null,website,null);


            }
            if(!mUserSettings.getSettings().getDescription().equals(description)){
                //Update Description
                mFirebaseMethods.updateUserAcccountSettings(null,null,description);


            }

        }

    /**
     * check if username already in database
     * @param username
     */
    private void checkIfUserNameExist(final String username) {
        Log.d(TAG, "checkIfUserNameExist: Checking If: " + username + " already exist ");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    //Update The UserName
                    mFirebaseMethods.updateUsername(username);

                }
                for (DataSnapshot singleSnapShot:dataSnapshot.getChildren()){
                    if (singleSnapShot.exists()){
                        Log.d(TAG, "checkIfUserNameExist: Found A Match: " + singleSnapShot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "that UserName Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        UserAccountSettings settings = userSettings.getSettings();
        Glide.with(getActivity()).load(settings.getProfile_photo()).into(mprofileImageView);
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Changing Profile Photo");
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

    }



    // ****************************** FIRE BASE ********************************//

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: Setting Up.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));


                //retrieve Images for the user in question
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
