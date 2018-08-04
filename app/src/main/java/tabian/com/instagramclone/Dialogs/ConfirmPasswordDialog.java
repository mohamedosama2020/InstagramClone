package tabian.com.instagramclone.Dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import tabian.com.instagramclone.R;


public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG = "ConfirmPasswordDialog";

    public interface OnConfirmPassowrdListener{
        public void onConfirmPassword(String password);
    }
    OnConfirmPassowrdListener mOnConfirmPassowrdListener;

    //vars
    TextView mPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogue_confirm_password,container,false);
        Log.d(TAG, "onCreateView: started. ");

        mPassword=view.findViewById(R.id.confirm_password);

        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Confirming Password");

                String password = mPassword.getText().toString();
                if(!password.equals("")){
                    mOnConfirmPassowrdListener.onConfirmPassword(password);
                    getDialog().dismiss();

                }else
                {
                    Toast.makeText(getActivity(), "You Must Enter Your Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView cancelDialog = view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Cancel Dialog");
                getDialog().dismiss();
            }
        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnConfirmPassowrdListener = (OnConfirmPassowrdListener) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.d(TAG, "onAttach: ClassCastException " + e.getMessage());

        }
    }
}
