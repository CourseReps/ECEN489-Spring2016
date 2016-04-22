package com.example.fanchaozhou.project3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Fanchao Zhou on 4/4/2016.
 */
public class TypeListDialogFragment extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);   //The dialog is not cancelable.
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        CharSequence[] typeNameList = new CharSequence[ MainActivity.typeList.size() ];
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        for(int cnt = 0; cnt < MainActivity.typeList.size(); cnt++){
            typeNameList[ cnt ] = MainActivity.typeList.get(cnt).typeName;
        }

        builder.setTitle(R.string.type_list_dialog_title)
                .setItems(typeNameList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), which, null);
                    }
                });
        return builder.create();
    }
}
