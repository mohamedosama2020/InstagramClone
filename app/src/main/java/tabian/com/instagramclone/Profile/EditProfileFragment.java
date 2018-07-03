package tabian.com.instagramclone.Profile;

import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.UniversalImageLoader;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    String imgURL = "https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";

    private ImageView mprofileImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mprofileImageView = (ImageView) view.findViewById(R.id.profile_photo);

        Glide.with(getContext()).load(imgURL).crossFade().into(mprofileImageView);
        //initImageLoader();
        //setProfileImage();
        return view;
    }


    /*
    private void initImageLoader(){

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    private void setProfileImage(){

        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL,mprofileImageView,null,"https://");

    }
    */
}
