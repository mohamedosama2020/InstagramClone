package tabian.com.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;

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

         mContext = getActivity();

         setupBottomNavigationView();
         setupToolbar();
         return view;
    }

        /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
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
}
