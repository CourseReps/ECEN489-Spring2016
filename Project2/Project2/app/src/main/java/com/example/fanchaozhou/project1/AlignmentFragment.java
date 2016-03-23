/**
 * @file AlignmentFragement.java
 *
 * @brief Used to show and calibrate the alignment of the phone
 *
 **/

/**
 * Created by Kyle on 2/22/16.
 */

package com.example.fanchaozhou.project1;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * @class AlignmentFragement
 *
 * @brief The brains behind the alignment fragment
 */
public class AlignmentFragment extends Fragment{

    private DataCollector dataStruct;
    private SharedPreferences sharedPref;
    public final static boolean IS_AUTO_RUNNING_DEF = false;
    public final static boolean IS_USED_DEF = false;
    public final static boolean IS_ALIGNED_DEF = false;
    public final static String IS_AUTO_RUNNING_PREF_KEY = "Auto Running Preference";
    public final static String IS_USED_PREF_KEY = "Is_Used Preference";
    public final static String IS_ALIGNED_PREF_KEY = "Is_Aligned Preference";
    public static final String SETTINGS_FILE = "SETTINGS_ON_MAINFRAGMENT";
    private SharedPreferences.Editor editor = null;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        /*Get the previous values of the settings*/
        boolean isAutoRunning = settings.getBoolean(IS_AUTO_RUNNING_PREF_KEY, IS_AUTO_RUNNING_DEF);
        boolean isAligned = settings.getBoolean(IS_ALIGNED_PREF_KEY, IS_ALIGNED_DEF);
        boolean isUsed = settings.getBoolean(IS_USED_PREF_KEY, IS_USED_DEF);
        /*Set the three checkboxes to the previous values*/
        CheckBox isAlignedCheckBox = (CheckBox)getActivity().findViewById(R.id.alignment_checkbox);
        isAlignedCheckBox.setChecked(isAligned);
        //CheckBox isAutoCheckBox = (CheckBox)getActivity().findViewById(TODO: the id of the checkbox for continuous running);
        CheckBox isUsedCheckBox = (CheckBox)getActivity().findViewById(R.id.alignment_used_checkbox);
    }
    /**
     * @fn onCreateView
     * @brief loads the alignment xml as a fragment into the MainFragment
     *
     * A Fragment inside another fragment may not be wise or Androidesque but it works for now.
     * Consider creating another activity.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alignment_fragment, container, false);

        if(savedInstanceState == null) {

            /*Set the three checkboxes to the previous values*/
            final CheckBox isAlignedCheckBox = (CheckBox) view.findViewById(R.id.alignment_checkbox);
            //final CheckBox isAutoCheckBox = (CheckBox)getActivity().findViewById(TODO: the id of the checkbox for continuous running);
            //final CheckBox isUsedCheckBox = (CheckBox)getActivity().findViewById(TODO: the id of the );
            //final Checkbox isAligned = (CheckBox)getActivity9).findViewById();TODO: needs to be used for something

            isAlignedCheckBox.setOnClickListener(new View.OnClickListener() {//Checkbox handler for alignment
                @Override
                public void onClick(View v) {
                    //Save the alignment checkbox status in the SharedPreferences File
                    editor.putBoolean(IS_ALIGNED_PREF_KEY, isAlignedCheckBox.isChecked());
                    editor.commit();
                }
            });
        }
        return view;
    }
}
