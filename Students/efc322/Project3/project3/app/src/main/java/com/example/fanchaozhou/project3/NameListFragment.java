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

public class NameListFragment extends Fragment {

    private static final int REQUEST_DELETION_TYPE = 1;
    private static final String REQUEST_DELETION_TAG = "Request Deletion Tag";
    private int itemPosition;
    private NameListAdapter nameListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            nameListAdapter = new NameListAdapter(getActivity(), R.layout.list_item_type, MainActivity.typeList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_name_list, container, false);

        if(savedInstanceState == null){
            ListView listView = (ListView)rootView.findViewById(R.id.name_list_listView);
            listView.setAdapter(nameListAdapter);
            final NameListFragment curFragment = this;

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!MainActivity.isInTraining){
                        Toast toast = Toast.makeText(getActivity(), "Deletion is NOT ALLOWED in RUNNING MODE", Toast.LENGTH_LONG);
                        toast.show();

                        return false;
                    } else {
                        MsgDialogFragment deletionDialog = new MsgDialogFragment();
                        Bundle messageBdl = new Bundle();
                        messageBdl.putString(MsgDialogFragment.DELETION_MSG_TAG, getString(R.string.delete_type_msg));
                        deletionDialog.setArguments(messageBdl);
                        deletionDialog.setTargetFragment(curFragment, REQUEST_DELETION_TYPE);
                        itemPosition = position;
                        deletionDialog.show(getActivity().getSupportFragmentManager(), REQUEST_DELETION_TAG);

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

        if(requestCode==REQUEST_DELETION_TYPE && resultCode== MsgDialogFragment.DELETION_POSITIVE){
            RecordDBHelper dbHelper = new RecordDBHelper(getActivity());
            for(int cnt = MainActivity.recordList.size()-1; cnt >= 0; cnt--){
                if(MainActivity.typeList.get(itemPosition).typeID == MainActivity.recordList.get(cnt).typeID){
                    File imageToDelete = new File(Uri.parse(MainActivity.recordList.get(cnt).fullsizePhotoUri).getPath());
                    imageToDelete.delete();
                    MainActivity.recordList.remove(cnt);
                }
            }
            dbHelper.deleteOneType(MainActivity.typeList.get(itemPosition).typeID);
            MainActivity.typeList.remove(itemPosition);

            nameListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        nameListAdapter.notifyDataSetChanged();
    }
}
