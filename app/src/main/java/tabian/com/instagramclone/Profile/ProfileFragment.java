package tabian.com.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tabian.com.instagramclone.Login.LoginActivity;
import tabian.com.instagramclone.Models.Photo;
import tabian.com.instagramclone.Models.User;
import tabian.com.instagramclone.Models.UserAccountSettings;
import tabian.com.instagramclone.Models.UserSettings;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.BottomNavigationViewEx;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;
import tabian.com.instagramclone.Utils.FirebaseMethods;
import tabian.com.instagramclone.Utils.ImageAdapter;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;


    //fierbase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private Context mContext;

    private TextView mPosts , mFollowers , mFollowing , mDisplayName , mUsername , mWebsite , mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Starting");
         View view = inflater.inflate(R.layout.fragment_profile,container,false);
         mDisplayName = view.findViewById(R.id.display_name);
         mUsername = view.findViewById(R.id.username);
         mWebsite = view.findViewById(R.id.website);
         mDescription = view.findViewById(R.id.description);
         mProfilePhoto = view.findViewById(R.id.profile_image);
         mPosts = view.findViewById(R.id.tvPosts);
         mFollowers = view.findViewById(R.id.tvFollowers);
         mFollowing = view.findViewById(R.id.tvFollowing);
         mProgressBar = view.findViewById(R.id.profileProgressBar);
         gridView = view.findViewById(R.id.gridView);
         toolbar = view.findViewById(R.id.profileToolBar);
         profileMenu = view.findViewById(R.id.profile_menu);
         bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
         TextView mEditProfile = view.findViewById(R.id.textEditProfile);
         mEditProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d(TAG, "onClick: Nvaigation To Edit Profile Fragment");
                 Intent intent = new Intent(getActivity(),AccountSettingsActivity.class);
                 intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_fragment));
                 startActivity(intent);
                 getActivity().overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
             }
         });

         mContext = getActivity();
         mFirebaseMethods = new FirebaseMethods(getActivity());

         setupBottomNavigationView();
         setupToolbar();
         setupFirebaseAuth();
         setupGridView();


         return view;
    }


    private void setupGridView(){

        Log.d(TAG, "setupGridView: setting up image grid");
        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    photos.add(singleSnapshot.getValue(Photo.class));
                }
                //setup our Image Grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<>();
                for(int i=0;i<photos.size();i++){
                    imgUrls.add(photos.get(i).getImage_path());
                }
                ImageAdapter adapter = new ImageAdapter(getActivity(),imgUrls,"");
                gridView.setAdapter(adapter);
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled");

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: "+userSettings.toString());

        UserAccountSettings settings = userSettings.getSettings();
        Glide.with(getActivity()).load(settings.getProfile_photo()).into(mProfilePhoto);
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mProgressBar.setVisibility(View.GONE);


    }



        /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

        private void setupToolbar(){
        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    // ****************************** FIRE BASE ********************************//

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: Setting Up.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

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
