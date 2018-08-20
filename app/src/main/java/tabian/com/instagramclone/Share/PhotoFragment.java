package tabian.com.instagramclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tabian.com.instagramclone.Profile.AccountSettingsActivity;
import tabian.com.instagramclone.R;

/**
 * Created by User on 5/28/2017.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int GALLERY_FRAGMENT_NUM = 0;
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int CAMERA_REQUEST_CODE = 5;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);


        Button btnLaunchCamera =  view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: launching camera");

                if(((ShareActivity)getActivity()).getCurrentTabNumber() == 1){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);

                }
            }
        });

        return view;
    }


    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }else
            return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){

            Log.d(TAG, "onActivityResult: Done Taking Photo.");
            Log.d(TAG, "onActivityResult: attemping to navigate to final share screen.");

            //navigate to the final share screen to publish photo

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");


            if(isRootTask()){

            }else
            {
                try {
                    Log.d(TAG, "onActivityResult: recieved new bitmap from camera: "+bitmap);
                    Intent intent = new Intent(getActivity(),AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap),bitmap);
                    intent.putExtra(getString(R.string.return_to_fragment),getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();

                }catch (NullPointerException e){
                    Log.d(TAG, "onActivityResult: NullPointerException"+e.getMessage());

                }
            }

        }
    }
}
