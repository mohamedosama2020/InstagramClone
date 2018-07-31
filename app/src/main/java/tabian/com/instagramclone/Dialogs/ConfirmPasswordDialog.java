package tabian.com.instagramclone.Dialogs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tabian.com.instagramclone.R;


public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG = "ConfirmPasswordDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogue_confirm_password,container,false);
        Log.d(TAG, "onCreateView: started. ");

        return view;

    }
}
