package tabian.com.instagramclone;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tabian.com.instagramclone.Models.Photo;
import tabian.com.instagramclone.Utils.BottomNavigationViewEx;
import tabian.com.instagramclone.Utils.BottomNavigationViewHelper;
import tabian.com.instagramclone.Utils.SquareImageView;

public class ViewPostFragment extends Fragment {

    private static final String TAG = "ViewPostFragment";

    //widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mBackLabel,mCaption,mUsername,mTimestamp;
    private ImageView mBackArrow,mEllipses,mHeartRed,mHeartWhite,mProfileImage;

    //vars
    private Photo mPhoto;
    private int mActivityNumber = 0;

    public ViewPostFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post,container,false);
        mPostImage =  view.findViewById(R.id.post_image);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        mBackLabel = view.findViewById(R.id.tvBackLabel);
        mCaption = view.findViewById(R.id.image_caption);
        mUsername = view.findViewById(R.id.username);
        mTimestamp = view.findViewById(R.id.image_time_posted);
        mEllipses = view.findViewById(R.id.ivEllipses);
        mHeartRed = view.findViewById(R.id.image_heart_red);
        mHeartWhite = view.findViewById(R.id.image_heart);
        mProfileImage = view.findViewById(R.id.profile_photo);



        try{
            mPhoto = getPhotoFromBundle();
            Glide.with(getActivity()).load(mPhoto.getImage_path()).into(mPostImage);
            mActivityNumber = getActivityNumFromBundle();

        }catch (NullPointerException e){

            Log.d(TAG, "onCreateView: NullPointerException: " + e.getMessage());

        }
        setupBottomNavigationView();
        return view;
    }


    /**
     * retrieve the activity number from the incoming bundle from profileActivity interface
     * @return
     */
    private int getActivityNumFromBundle() {


        Log.d(TAG, "getPhotoFromBundle: arguments: "+ getArguments());
        Bundle bundle = this.getArguments();
        if(bundle != null){

            return bundle.getInt(getString(R.string.activity_number));

        }else {
            return 0;
        }    }

    /**
     * retrieve the photo from the incoming bundle from profileActivity interface
     * @return
     */
    private Photo getPhotoFromBundle(){

        Log.d(TAG, "getPhotoFromBundle: arguments: "+ getArguments());
        Bundle bundle = this.getArguments();
        if(bundle != null){

            return bundle.getParcelable(getString(R.string.photo));

        }else {
            return null;
        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(getActivity(),getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
    }

}
