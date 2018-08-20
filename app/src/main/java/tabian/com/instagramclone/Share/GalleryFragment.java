package tabian.com.instagramclone.Share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import tabian.com.instagramclone.Profile.AccountSettingsActivity;
import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.FilePaths;
import tabian.com.instagramclone.Utils.FileSearch;
import tabian.com.instagramclone.Utils.ImageAdapter;

/**
 * Created by User on 5/28/2017.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";


    //Constants
    private static final int NUM_GRID_COLUMNS = 3;


    //Widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file://";
    private String mSelectedImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Started");
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        galleryImage = view.findViewById(R.id.galleryImageView);
        gridView = view.findViewById(R.id.gridView);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        directories = new ArrayList<>();

        ImageView shareClose = view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Closing the Gallery Fragment");
                getActivity().finish();
            }
        });

        TextView nextScreen = view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to the final share screen");

                if(isRootTask()){
                    Intent intent = new Intent(getActivity(),NextActivity.class);
                    intent.putExtra(getString(R.string.selected_image),mSelectedImage);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(getActivity(),AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_image),mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment),getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        init();


        return view;
    }


    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }else
            return false;
    }

    private void init(){

        FilePaths filePaths = new FilePaths();
        //check for other folders inside "/storage/emulated/0/pictures"
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        //Take The Last Word After The Last Slash "/" To Be Displayed In The Spinner
        ArrayList<String> directoryNames = new ArrayList<>();
        for(int i=0;i<directories.size();i++){

            int slashIndex = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(slashIndex);
            directoryNames.add(string);
        }


        directories.add(filePaths.CAMERA);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: Selected " + directories.get(i));

                //setup our image grid for the directory chosen
                setupGridView(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void setupGridView(String selectedDirectory){

        Log.d(TAG, "setupGridView: Directory Chosen"  + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth  = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imgWidth);

        //use grid adapter to adapter the images in GridvIew

        ImageAdapter adapter = new ImageAdapter(getActivity() , imgURLs ,mAppend);
        gridView.setAdapter(adapter);

        //set the first image to be diplayed when the activity fragment is inflated
        setImage(imgURLs.get(0),galleryImage,mAppend);
        mSelectedImage = imgURLs.get(0);

        //Grid View Listner
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: You Selected Image"+imgURLs.get(i));
                setImage(imgURLs.get(i),galleryImage,mAppend);
                mSelectedImage = imgURLs.get(i);


            }
        });
    }




    private void setImage(String imageURL , ImageView image , String append){

        mProgressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "setImage: Setting Image");
        Glide.with(getActivity()).load(append+imageURL).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                return false;

            }
        }).placeholder(R.drawable.ic_image_placeholder).into(image);
    }
}
