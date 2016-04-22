package com.example.fanchaozhou.project3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class SamplesFragment extends Fragment {

    private static final int REQUEST_DELETION_RECORD = 1;
    private static final String DELETION_RECORD_TAG = "Delete a single Record";
    private int itemPosition;
    SamplesAdapter samplesAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            samplesAdapter = new SamplesAdapter(getActivity(), R.layout.list_item_sample, MainActivity.recordList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sample_list, container, false);

        if(savedInstanceState == null){
            ListView listView = (ListView)rootView.findViewById(R.id.sample_list_listView);
            listView.setAdapter(samplesAdapter);
            final SamplesFragment curFragment = this;

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!MainActivity.isInTraining){
                        Toast toast = Toast.makeText(getActivity(), "Deletion is NOT ALLOWED in RUNNING MODE", Toast.LENGTH_LONG);
                        toast.show();

                        return false;
                    } else {
                        itemPosition = position;
                        MsgDialogFragment deletionDialog = new MsgDialogFragment();
                        Bundle messageBdl = new Bundle();
                        messageBdl.putString(MsgDialogFragment.DELETION_MSG_TAG, getString(R.string.delete_rec_msg));
                        deletionDialog.setArguments(messageBdl);
                        deletionDialog.setTargetFragment(curFragment, REQUEST_DELETION_RECORD);
                        itemPosition = position;
                        deletionDialog.show(getFragmentManager(), DELETION_RECORD_TAG);

                        return true;
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_DELETION_RECORD && resultCode== MsgDialogFragment.DELETION_POSITIVE){
            RecordDBHelper dbHelper = new RecordDBHelper(getActivity());
            File imageToDelete = new File(Uri.parse(MainActivity.recordList.get(itemPosition).fullsizePhotoUri).getPath());
            imageToDelete.delete();
            dbHelper.deleteOneRec(MainActivity.recordList.get(itemPosition).recordID);
            MainActivity.recordList.remove(itemPosition);

            samplesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        samplesAdapter.notifyDataSetChanged();
    }
}
