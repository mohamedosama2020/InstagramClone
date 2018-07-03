package tabian.com.instagramclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;
import tabian.com.instagramclone.Utils.ImageAdapter;

/**
 * Created by User on 5/28/2017.
 */

public class ProfileActivity extends AppCompatActivity{
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;

    GridView gridView;
    private Context mContext = ProfileActivity.this;
    private ProgressBar mpProgressBar;
    public static String[] imgURLs = {
            "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlkaSbo2TCG8DIiEtyKIPy590GgDHZvFLmfjPuUPCp8NIa7dZh",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmoVHYVADxDUGTEruk-8Jn6NMrSeo_Qx9N9fEnzJz9M4dwBqdR",
            "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlkaSbo2TCG8DIiEtyKIPy590GgDHZvFLmfjPuUPCp8NIa7dZh",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmoVHYVADxDUGTEruk-8Jn6NMrSeo_Qx9N9fEnzJz9M4dwBqdR",
            "https://www.apple.com/ac/structured-data/images/knowledge_graph_logo.png?201709101434",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScCI6DcwLRSWB3Kut0u8_fxgmZBZmwV4Xu139M-j-gpWLXhIB0YQ",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlY_ZKT9QMQk3ndb5gqNPH2B9g5Z0rysklA56gmt-J2uDDpufBPg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmoVHYVADxDUGTEruk-8Jn6NMrSeo_Qx9N9fEnzJz9M4dwBqdR",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlY_ZKT9QMQk3ndb5gqNPH2B9g5Z0rysklA56gmt-J2uDDpufBPg",
            "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo.jpg"

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        gridView = findViewById(R.id.gridView);
        mpProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mpProgressBar.setVisibility(View.GONE);

        setupProfileImage();
        setupBottomNavigationView();
        setupToolbar();

        gridView.setAdapter(new ImageAdapter(this,imgURLs));
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        ImageView profileMenu = (ImageView) findViewById(R.id.profile_menu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
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

    private void setupProfileImage(){

        CircleImageView profilePhoto = findViewById(R.id.profile_image);
        Glide.with(getApplicationContext()).load("https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf").placeholder(R.drawable.ic_image_placeholder).crossFade(1000).into(profilePhoto);    }
}
