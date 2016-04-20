package com.example.fanchaozhou.project3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Fanchao Zhou on 4/9/2016.
 */
public class MsgDialogFragment extends DialogFragment {
    public static final int DELETION_POSITIVE = 1;
    public static final String DELETION_MSG_TAG = "Deletion Message Tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Warning");
        builder.setMessage(getArguments().getString(DELETION_MSG_TAG));  //Set the message sent by the main fragment
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            //Configure the name and handler for positive button
            @Override
            public void onClick(DialogInterface dialog, int which) {  //Set the handler for "OK" button
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        DELETION_POSITIVE,
                        null);  //Send the intent and result code back to the ImageFragment
                dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}
