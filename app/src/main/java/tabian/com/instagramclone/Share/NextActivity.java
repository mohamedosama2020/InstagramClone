package tabian.com.instagramclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tabian.com.instagramclone.R;
import tabian.com.instagramclone.Utils.FirebaseMethods;

public class NextActivity extends AppCompatActivity {
    private static final String TAG = "NextActivity";


    //fierbase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener maAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //Widgets
    private EditText mCaption;

    //vars
    private String mAppend = "file://";
    private int imgCount = 0;
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        setupFirebaseAuth();

        mFirebaseMethods = new FirebaseMethods(NextActivity.this);


        mCaption = findViewById(R.id.caption);
        ImageView backArrow = findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Closing the Activity");
                finish();
            }
        });

        TextView share = findViewById(R.id.tvShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attemping To Upload new Photo ");
                //Upload The Image To The FireBase
                String caption = mCaption.getText().toString();

                if(intent.hasExtra(getString(R.string.selected_image))){

                    imgUrl = intent.getStringExtra(getString(R.string.selected_image));
                    mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption , imgCount , imgUrl,null);

                }else if (intent.hasExtra(getString(R.string.selected_bitmap))){

                    bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption , imgCount , null,bitmap);

                }
            }
        });

        setImage();


    }



    private void setImage(){
        intent= getIntent();
        ImageView image = findViewById(R.id.imageShare);

        if(intent.hasExtra(getString(R.string.selected_image))){

            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            Log.d(TAG, "setImage: got new image url " + imgUrl);
            Glide.with(getApplication()).load(imgUrl).into(image);

        }else if (intent.hasExtra(getString(R.string.selected_bitmap))){

            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG, "setImage: got new bitmap ");
            image.setImageBitmap(bitmap);

        }
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

                imgCount = mFirebaseMethods.getImgCount(dataSnapshot);
                Log.d(TAG, "onDataChange: image count = " + imgCount);


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
