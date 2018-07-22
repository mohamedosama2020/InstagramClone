package tabian.com.instagramclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tabian.com.instagramclone.Models.User;
import tabian.com.instagramclone.Models.UserAccountSettings;
import tabian.com.instagramclone.Models.UserSettings;
import tabian.com.instagramclone.R;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Context mContext;
    private String userID;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public boolean  checkIfUsernameExists(String username , DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfUsernameExists:  checking if "+username+" alreaady exists");
        User user = new User();
        for (DataSnapshot ds:dataSnapshot.child(userID).getChildren()){
            Log.d(TAG, "checkIfUsernameExists: datasnoapshot : "+ds);
            user.setUsername(ds.getValue(User.class).getUsername());

            if (StringManuplation.expandUsername((user.getUsername())).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: Found A Match" + user.getUsername());

                return true;
            }

        }
        return false;

    }

    /**
     * Add Information of user node
     * Add Information of user_account_settings node
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */

    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User( userID,  1,  email,  StringManipulation.condenseUsername(username) );

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);


        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                username,
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }

    /**
     * Register New Email And Password to Firebase Auth
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email , String password , final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            sendVerificationEmail();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ....
                    }
                });


    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(mContext, "Check Your Email To Verify Your Account..", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Toast.makeText(mContext, "Couldn't send verification email..", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }


    /***
     * Retrievs the account settings from user
     * Database: user_account_settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){

        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase");


        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();
        for(DataSnapshot ds: dataSnapshot.getChildren()){

            //user_account_settings node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
                Log.d(TAG, "getUserAccountSettings: datasnapshot: = "+ds);


                try{

                    settings.setDisplay_name(ds.child(userID).getValue(UserAccountSettings.class).getDisplay_name());
                    settings.setUsername(ds.child(userID).getValue(UserAccountSettings.class).getUsername());
                    settings.setWebsite(ds.child(userID).getValue(UserAccountSettings.class).getWebsite());
                    settings.setDescription(ds.child(userID).getValue(UserAccountSettings.class).getDescription());
                    settings.setProfile_photo(ds.child(userID).getValue(UserAccountSettings.class).getProfile_photo());
                    settings.setPosts(ds.child(userID).getValue(UserAccountSettings.class).getPosts());
                    settings.setFollowers(ds.child(userID).getValue(UserAccountSettings.class).getFollowers());
                    settings.setFollowing(ds.child(userID).getValue(UserAccountSettings.class).getFollowing());

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information : "+settings.toString());



                }catch ( NullPointerException e){
                    Log.e(TAG, "getUserAccountSettings: NullPointerException"+e.getMessage() );
                }


                //user_account_settings node
                if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                    Log.d(TAG, "getUserAccountSettings: datasnapshot: = " + ds);

                    user.setUsername(ds.child(userID).getValue(User.class).getUsername());
                    user.setEmail(ds.child(userID).getValue(User.class).getEmail());
                    user.setPhone_number(ds.child(userID).getValue(User.class).getPhone_number());
                    user.setUser_id(ds.child(userID).getValue(User.class).getUser_id());

                    Log.d(TAG, "getUserAccountSettings: retrieved users information : "+user.toString());


                }



            }
        }
        return new UserSettings(user,settings);

    }











}
