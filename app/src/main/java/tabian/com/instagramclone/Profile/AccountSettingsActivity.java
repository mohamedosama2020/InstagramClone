package tabian.com.instagramclone.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;

import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.BottomNavigationViewEx;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;
import tabian.com.instagramclone.Utils.FirebaseMethods;
import tabian.com.instagramclone.Utils.SectionsStatePagerAdapter;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";
    private Context mContext;
    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private static final int ACTIVITY_NUM = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        mContext = AccountSettingsActivity.this;
        mViewPager = (ViewPager) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);


        setupSettingList();
        setupFragments();
        setupBottomNavigationView();
        getIncomingIntent();
        //Setup Back Arrow to get back to Account Settings Activity...

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: navigation back to ProfileActivity");
                finish();
            }
        });
    }

    private void getIncomingIntent(){

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "getIncomingIntent: Recieved Incoming Intent from " + getString(R.string.profile_activity));

            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
    }

    private void setupFragments(){
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(),"EditProfileFragment"); // fragment 0
        pagerAdapter.addFragment(new SignOutFragment(),"SignOutFragment");         // fragment 1


    }

    private void setViewPager(int fragmentNumber){
        mRelativeLayout.setVisibility(View.GONE);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void setupSettingList(){

        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);
        ArrayList<String> options = new ArrayList<>();
        options.add("Edit Profile"); // fragment 0
        options.add("Sign Out");      // fragment 1

        ArrayAdapter adapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setViewPager(i);
            }
        });
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
