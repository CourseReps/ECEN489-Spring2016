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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

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

    private TextView yawText;
    private TextView rollText;
    private TextView pitchText;
    private Boolean paused = false;
    private Square square = new Square();

    /**
     * @class onCreate
     *
     * @brief Method to run when fragment is created. It sets up a preference manager and runs a
     * thread to update the gui every 10ms
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating a Shared Preference Manager
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPref.edit();

        /* orientation graphic thread */
        Thread thread = new Thread() {

            @Override
            public void run() {
                while (!paused){
                    try {
                        while (!isInterrupted()) {
                            while (!paused){ // stop when fragment is paused
                                Thread.sleep(10); // update every 10ms
                                getActivity().runOnUiThread(new Runnable() { // ui can only be updated from ui thread
                                @Override
                                public void run() {

                                    /* display yaw pitch and roll in a text view */
                                    yawText.setText(String.format(getString(R.string.yawsettext), (int) DataCollector.yaw));
                                    pitchText.setText(String.format(getString(R.string.pitchsettext), (int) DataCollector.pitch));
                                    rollText.setText(String.format(getString(R.string.rollsettext), (int) DataCollector.roll));

                                    /* check orientation and update graphic */
                                    updateGraphic();
                                }
                            });
                        }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

    }

    /**
     * @fn updateGraphic
     * @brief The update graphic routine is called in the ui update thread. It updates the text views
     * with the current yaw, pitch, and roll of the device. It then checks the current orientation
     * against the tolerances defined in the settings. It will color the ui graphic depending
     * on orientation, as well as mark a boolean field for the automatic data collection function
     */
    public void updateGraphic(){

        /* set up constants for orientation graphic */
        String pitchTolString;
        String rollTolString;
        float red[] = {0.85f, 0.0f, 0.0f, 1.0f};
        float green[] = {0.57843137f, 0.83921569f, 0.0f, 1.0f};
        float color[] = {0.0f, 0.0f, 0.0f, 1.0f}; // used for variable gradient
        float pitchTol;
        float rollTol;
        float PITCH_MIN;
        float PITCH_MAX;
        float pitchError = 0;
        float rollError;
        boolean checkbox;

        /* creating toast notifications */
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence pauseText = "collection paused";
        CharSequence resumeText = "collection resumed";
        Toast pauseToast = Toast.makeText(context, pauseText, duration);
        Toast resumeToast = Toast.makeText(context, resumeText, duration);



        /* Get orientation tolerances from preferences */
        pitchTolString = sharedPref.getString(getString(R.string.pref_tolerance_theta_key), "10");
        rollTolString = sharedPref.getString(getString(R.string.pref_tolerance_phi_key), "5");
        checkbox = sharedPref.getBoolean(IS_ALIGNED_PREF_KEY, false); // check if checkbox is checked

        /* Handle inputs that are not parsable floats */
        try {
            pitchTol = Float.parseFloat(pitchTolString);
            rollTol = Float.parseFloat(rollTolString);
        }catch(NumberFormatException e){
            e.printStackTrace();
            pitchTol = 10.0f;
            rollTol = 5.0f;
        }

        /* Make sure the tolerances are acceptable values */
        if(!((0<=pitchTol)&&(pitchTol<=90))){
            pitchTol = 10.0f;
        }
        if(!((0<=rollTol)&&(rollTol<=360))){
            rollTol = 5.0f;
        }

        /* Set acceptable orientation boundaries */
        PITCH_MIN = 90 - pitchTol;
        PITCH_MAX = 90 + pitchTol;

        /* Set orientation graphic color based on current orientation and set boundaries */
        if((PITCH_MIN<(Math.abs(DataCollector.pitch)))&&((Math.abs(DataCollector.pitch))<PITCH_MAX)&&((Math.abs(DataCollector.roll))<rollTol)){
            square.setColor(green); // set green if within the set tolerance
            if(checkbox && !DataCollector.aligned && DataCollector.contCollection){ // if the device goes from out of alignment to in alignment
                resumeToast.show();                  // make a toast notification to notify of collection resume
            }
            DataCollector.aligned = true;
        }
        else if(((90-2*pitchTol)>(Math.abs(DataCollector.pitch)))||((Math.abs(DataCollector.pitch))>(90+2*pitchTol))||((Math.abs(DataCollector.roll))>(2*rollTol))){
            square.setColor(red); // set red if outside boundary described above
            if(checkbox && DataCollector.aligned && DataCollector.contCollection){  // if the device goes from in alignment to out of alignment
                pauseToast.show();                  // make a toast notification to notify of collection pause
            }
            DataCollector.aligned = false;
        }
        else{ // if between tolerance and boundary, gradient from red to green
            if(Math.abs(DataCollector.pitch)<90){
                pitchError =  Math.abs(90.0f - pitchTol - Math.abs(DataCollector.pitch))/pitchTol;
            }
            else if(Math.abs(DataCollector.pitch)>90){
                pitchError =  Math.abs(90.0f + pitchTol - Math.abs(DataCollector.pitch))/pitchTol;
            }
            if(Math.abs(DataCollector.roll)<rollTol){
                rollError = 0;
            }
            else{
                rollError =  Math.abs(Math.abs(DataCollector.roll) - rollTol)/(rollTol);
            }
            color[0] = 0.57843137f + 0.27156863f * (pitchError/2 + rollError/2);
            color[1] = 0.83921569f - 0.83921569f * (pitchError/2 + rollError/2);
            square.setColor(color);
            if(checkbox && DataCollector.aligned && DataCollector.contCollection){  // if the device goes from in alignment to out of alignment
                pauseToast.show();                  // make a toast notification to notify of collection pause
            }
            DataCollector.aligned = false;
        }

        if(!checkbox){
            DataCollector.aligned = true; // if the checkbox is not checked, auto collect data regardless of alignment
        }
    }

    /**
     * @fn onStart
     * @brief Method to be run when the fragment starts
     */
    @Override
    public void onStart()
    {
        super.onStart();

        /*Get the previous values of the settings*/
        boolean isAutoRunning = sharedPref.getBoolean(IS_AUTO_RUNNING_PREF_KEY, IS_AUTO_RUNNING_DEF);
        boolean isAligned = sharedPref.getBoolean(IS_ALIGNED_PREF_KEY, IS_ALIGNED_DEF);

        /*Set the three checkboxes to the previous values*/
        CheckBox isAlignedCheckBox = (CheckBox)getActivity().findViewById(R.id.alignment_checkbox);
        isAlignedCheckBox.setChecked(isAligned);
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

        //create text views
        yawText = (TextView) view.findViewById(R.id.yawText);
        pitchText = (TextView) view.findViewById(R.id.pitchText);
        rollText = (TextView) view.findViewById(R.id.rollText);

        if(savedInstanceState == null) {

            /*Set the three checkboxes to the previous values*/
            final CheckBox isAlignedCheckBox = (CheckBox) view.findViewById(R.id.alignment_checkbox);
            isAlignedCheckBox.setOnClickListener(new View.OnClickListener() {//Checkbox handler for alignment
                @Override
                public void onClick(View v) {
                    //Save the alignment checkbox status in the SharedPreferences File
                    editor.putBoolean(IS_ALIGNED_PREF_KEY, isAlignedCheckBox.isChecked());
                    editor.commit();
                    editor.apply();
                }
            });
        }
        return view;
    }

    /**
     * @fn onPause
     * @brief Method to be run when the fragment is paused. It marks a boolean field that is used
     * to pause the thread
     */
    @Override
    public void onPause(){
        super.onPause();
        paused = true;
    }

    /**
     * @fn onResume
     * @brief Method to be run when the fragment is resumed. It unmarks a boolean field that is used
     * to pause the thread
     */
    @Override
    public void onResume(){
        super.onResume();
        paused = false;
    }
}
