package com.example.fanchaozhou.project3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

/**
 * Created by Fanchao Zhou on 4/4/2016.
 */
public class AddTypesDialogFragment extends DialogFragment {
    public static final int ADDING_TYPES_DIALOG_POSRES = 1;    //Code for Positive Choice
    public static final String TYPE_NAME = "type name";        //Key Value

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.add_types_dialog_title));

        //Create a text view and prompt the user to enter the name of the new type
        final EditText dialogView = new EditText(getContext());
        dialogView.setHint("Enter the type name HERE");
        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //Configure the name and handler for positive button
            @Override
            public void onClick(DialogInterface dialog, int which) {  //Set the handler for "OK" button
                Intent intent = new Intent();
                intent.putExtra(TYPE_NAME, dialogView.getText().toString());  //Put the new name entered by the user in an intent
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        ADDING_TYPES_DIALOG_POSRES,
                        intent);  //Send the intent and result code back to the ImageFragment
                dismiss();
            }
        });

        return builder.create();
    }
}
