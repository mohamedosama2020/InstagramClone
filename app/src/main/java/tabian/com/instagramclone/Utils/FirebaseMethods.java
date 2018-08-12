package tabian.com.instagramclone.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

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
    private StorageReference mStorageReference;
    private DatabaseReference myRef;

    //vars
    private Context mContext;
    private String userID;
    private double  mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        myRef = mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }



    public void uploadNewPhoto(String photoType, String caption, int imgCount, String imgUrl) {

        Log.d(TAG, "uploadNewPhoto: Attemping to upload photo.");

        FilePaths filePaths = new FilePaths();

        //case 1) new photo
        if(photoType.equals(mContext.getString(R.string.new_photo))){

            Log.d(TAG, "uploadNewPhoto: uploading new photo.");

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (imgCount+1));

            // convert image url to bitmap
            Bitmap bm = ImageManager.getBitmap(imgUrl);
            byte[] bytes = ImageManager.getBytesFromBitmap(bm,100);


            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                    Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                    // add the new photo to "photo"  node and "user_photos" node


                    //navigate to the main feed so the user can see their photo


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: Photo uploading failed");
                    Toast.makeText(mContext, "Photo uploading failed" , Toast.LENGTH_SHORT).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) /taskSnapshot.getTotalByteCount();

                    if(progress-15 >  mPhotoUploadProgress){
                        Toast.makeText(mContext, "Photo upload progress" + String.format("%.0f" , progress) + "%", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");

                }
            });

        }

        //case 2) profile photo
        else if(photoType.equals(mContext.getString(R.string.profile_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading profile photo");

        }

    }



    public int getImgCount(DataSnapshot dataSnapshot){
        int count = 0 ;
        for (DataSnapshot ds: dataSnapshot.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count ++;

        }
        return count;
    }


    /**
     * Update "user_account_settings" node for the current user
     * @param displayName
     * @param website
     * @param description
     */
    public void updateUserAcccountSettings(String displayName , String website , String description){

        Log.d(TAG, "updateUserAcccountSettings: Updating UserAccountSettings");
        if (displayName != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }

        if (website != null)
        {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }

        if (description != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }

    }


    /**
     * Update username in the "Users" and "account_account_settings" nodes
     * @param username
     */
    public void updateUsername(String username){

        Log.d(TAG, "updateUsername: updating username to " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        Toast.makeText(mContext, "Saved Changes", Toast.LENGTH_SHORT).show();
    }


    /**
     * Update the Email in the "users" node
     * @param email
     */
    public void updateEmail(String email){

        Log.d(TAG, "updateUsername: updating email to " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }


//    public boolean  checkIfUsernameExists(String username , DataSnapshot dataSnapshot){
//        Log.d(TAG, "checkIfUsernameExists:  checking if "+username+" alreaady exists");
//        User user = new User();
//        for (DataSnapshot ds:dataSnapshot.child(userID).getChildren()){
//            Log.d(TAG, "checkIfUsernameExists: datasnoapshot : "+ds);
//            user.setUsername(ds.getValue(User.class).getUsername());
//
//            if (StringManuplation.expandUsername((user.getUsername())).equals(username)){
//                Log.d(TAG, "checkIfUsernameExists: Found A Match" + user.getUsername());
//
//                return true;
//            }
//
//        }
//        return false;
//
//    }

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
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: = " + ds);


                try {

                    settings.setDisplay_name(ds.child(userID).getValue(UserAccountSettings.class).getDisplay_name());
                    settings.setUsername(ds.child(userID).getValue(UserAccountSettings.class).getUsername());
                    settings.setWebsite(ds.child(userID).getValue(UserAccountSettings.class).getWebsite());
                    settings.setDescription(ds.child(userID).getValue(UserAccountSettings.class).getDescription());
                    settings.setProfile_photo(ds.child(userID).getValue(UserAccountSettings.class).getProfile_photo());
                    settings.setPosts(ds.child(userID).getValue(UserAccountSettings.class).getPosts());
                    settings.setFollowers(ds.child(userID).getValue(UserAccountSettings.class).getFollowers());
                    settings.setFollowing(ds.child(userID).getValue(UserAccountSettings.class).getFollowing());

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information : " + settings.toString());


                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException" + e.getMessage());
                }
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
        return new UserSettings(user,settings);

    }



}
