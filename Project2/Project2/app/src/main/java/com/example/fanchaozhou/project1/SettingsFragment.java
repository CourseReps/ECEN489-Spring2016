/**
 * @file SettingsFragment.java
 *
 * @brief Reads in Preferences from all the toggle widgets in the Settings Fragment
 *
**/

package com.example.fanchaozhou.project1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

/**
 * Created by Fanchao Zhou on 2/21/2016.
 */

/**
 * @class SettingsFragment
 *
 * @brief initializes view, looks for updates in preferences
 *
 * A more detailed class description
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            addPreferencesFromResource(R.xml.pref_general);
        }
    }

    /**
     * @fn onCreateView
     * @brief Setting up the items in the settings list by registering listeners
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if(savedInstanceState == null){
            Preference datatxPref = findPreference(getString(R.string.pref_http_key));
            datatxPref.setOnPreferenceChangeListener(this);   //Registering a listener for this item
            Preference serialBaudPref = findPreference(getString(R.string.pref_serial_baudrate_key));
            serialBaudPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
        }

        return rootView;
    }

    /**
     * @fn onStart
     * @brief The following code in this method is for displaying the values of all the settings whenever the app switches to the settings page
     *
     * This method will automatically save the values to com.example.fanchaozhou.project1_preference.xml. The app will reload the setting information saved before.
     * Clicking reset button will reset everything.
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Preference defaultSettings = findPreference("mybutton");
        //   defaultSettings.setOnPreferenceClickListener(this);
        defaultSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (preference.getKey().equals("mybutton")) {

                    EditTextPreference e1;
                    e1 = (EditTextPreference) findPreference(getString(R.string.pref_http_key));
                    String s1 = getString(R.string.pref_http_default);
                    e1.setText(s1);
                    e1.setSummary(s1);
                    e1.getText();

                    EditTextPreference e2;
                    e2 = (EditTextPreference) findPreference(getString(R.string.pref_serial_baudrate_key));
                    String s2 = getString(R.string.pref_serial_baudrate_default);
                    e2.setText(s2);
                    e2.setSummary(s2);
                    e2.getText();

                    EditTextPreference e3;
                    e3 = (EditTextPreference) findPreference(getString(R.string.pref_port_numb_key));
                    String s3 = getString(R.string.pref_port_numb_default);
                    e3.setText(s3);
                    e3.setSummary(s3);
                    e3.getText();

                    EditTextPreference e4;
                    e4 = (EditTextPreference) findPreference(getString(R.string.pref_s1_id_key));
                    String s4 = getString(R.string.pref_s1_id_default);
                    e4.setText(s4);
                    e4.setSummary(s4);
                    e4.getText();

                    EditTextPreference e5;
                    e5 = (EditTextPreference) findPreference(getString(R.string.pref_s2_id_key));
                    String s5 = getString(R.string.pref_s2_id_default);
                    e5.setText(s5);
                    e5.setSummary(s5);
                    e5.getText();

                    EditTextPreference e7;
                    e7 = (EditTextPreference) findPreference(getString(R.string.pref_init_table_key));
                    String s7 = "False";
                    e7.setText(s7);
                    e7.setSummary(s7);
                    e7.getText();


                    EditTextPreference e8;
                    e8 = (EditTextPreference) findPreference(getString(R.string.pref_table_name_key));
                    String s8 = getString(R.string.pref_table_name_default);
                    e8.setText(s8);
                    e8.setSummary(s8);
                    e8.getText();


                    EditTextPreference e9;
                    e9 = (EditTextPreference) findPreference(getString(R.string.pref_tolerance_theta_key));
                    String s9 = getString(R.string.pref_tolerance_theta_default);
                    e9.setText(s9);
                    e9.setSummary(s9);
                    e9.getText();

                    EditTextPreference e10;
                    e10 = (EditTextPreference) findPreference(getString(R.string.pref_tolerance_phi_key));
                    String s10 = getString(R.string.pref_tolerance_phi_default);
                    e10.setText(s10);
                    e10.setSummary(s10);
                    e10.getText();

                    CheckBoxPreference box1;
                    box1 = (CheckBoxPreference) findPreference(getString(R.string.pref_gps_changes_key));
                    box1.setChecked(false);

                    CheckBoxPreference box2;
                    box2 = (CheckBoxPreference) findPreference(getString(R.string.pref_rss_s1_key));
                    box2.setChecked(false);

                    CheckBoxPreference box3;
                    box3 = (CheckBoxPreference) findPreference(getString(R.string.pref_rss_s2_key));
                    box3.setChecked(false);

                    CheckBoxPreference box4;
                    box4 = (CheckBoxPreference) findPreference(getString(R.string.pref_gyro_key));
                    box4.setChecked(false);

                    CheckBoxPreference box5;
                    box5 = (CheckBoxPreference) findPreference(getString(R.string.pref_mag_key));
                    box5.setChecked(false);

                    CheckBoxPreference box6;
                    box6 = (CheckBoxPreference) findPreference(getString(R.string.pref_compass_key));
                    box6.setChecked(false);

                    MultiSelectListPreference list1;
                    list1 = (MultiSelectListPreference) findPreference(getString(R.string.pref_json_changes_key));
                    list1.setValues(new HashSet<String>());
                }
                return false;
            }
        });//Registering a listener for this item

        String serverAddr = sharedPref.getString(getString(R.string.pref_http_key), getString(R.string.pref_http_default));  //Get the server Address
        findPreference(getString(R.string.pref_http_key)).setSummary(serverAddr);
        String baudRate = sharedPref.getString(getString(R.string.pref_serial_baudrate_key), getString(R.string.pref_serial_baudrate_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_serial_baudrate_key)).setSummary(baudRate);
    }

    /**
     * @fn onPreferenceChange
     * @brief This method is invoked whenever a change has been made to any of the settings items
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(getString(R.string.pref_http_key))){  //If the server address is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);    //Setting the summary of an item
        } else if(preference.getKey().equals(getString(R.string.pref_serial_baudrate_key))) {  //If the baud rate is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        }

        return true;
    }
}
