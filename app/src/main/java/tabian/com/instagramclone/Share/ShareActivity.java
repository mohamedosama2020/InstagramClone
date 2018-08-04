package tabian.com.instagramclone.Share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.BottomNavigationViewEx;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;
import tabian.com.instagramclone.Utils.Permissions;
import tabian.com.instagramclone.Utils.SectionsPagerAdapter;

/**
 * Created by User on 5/28/2017.
 */

public class ShareActivity extends AppCompatActivity{
    private static final String TAG = "ShareActivity";
    private static final int ACTIVITY_NUM = 2;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private Context mContext = ShareActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();

        }else{
            verifyingPermissions(Permissions.PERMISSIONS);
        }

        //setupBottomNavigationView();
    }


    /**
     * return the current tab number
     * 0= GalleryFragment
     * 1= PhotoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * setup Viewpager for managing the tabs
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Gallery");
        tabLayout.getTabAt(1).setText("Photo");

    }


    /**
     * Verify All The Pemissions passed to the array
     * @param permissions
     */
    private void verifyingPermissions(String[] permissions) {
        Log.d(TAG, "verifyingPermissions: verifying permissions");
        ActivityCompat.requestPermissions(ShareActivity.this,permissions,1);
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    private boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: Checking Permissions Array");
        for(int i=0;i<permissions.length;i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }

        }
        return true ;
    }

    /**
     * Checks a Single Permission is it have verified.
     * @param permission
     * @return
     */

    private boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: Checking Permission: " + permission );

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for "+ permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission granted for "+ permission);
            return true;
        }
    }


    /**
     * BottomNavigationView setup
     */
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
