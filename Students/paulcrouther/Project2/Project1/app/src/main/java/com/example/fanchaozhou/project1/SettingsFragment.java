package com.example.fanchaozhou.project1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fanchao Zhou on 2/21/2016.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            addPreferencesFromResource(R.xml.pref_general);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if(savedInstanceState == null){
            //Setting up the items in the settings list
            //JSON Settings
            Preference jsonSettingsPref = findPreference(getString(R.string.pref_json_changes_key));
            jsonSettingsPref.setOnPreferenceChangeListener(this);
            Preference gpsPref = findPreference(getString(R.string.pref_gps_changes_key));
            gpsPref.setOnPreferenceChangeListener(this);
            Preference rssS1Pref = findPreference(getString(R.string.pref_rss_s1_key));
            rssS1Pref.setOnPreferenceChangeListener(this);
            Preference rssS2Pref = findPreference(getString(R.string.pref_rss_s2_key));
            rssS2Pref.setOnPreferenceChangeListener(this);
            Preference gyroPref = findPreference(getString(R.string.pref_gyro_key));
            gyroPref.setOnPreferenceChangeListener(this);
            Preference magPref = findPreference(getString(R.string.pref_mag_key));
            magPref.setOnPreferenceChangeListener(this);
            Preference compassPref = findPreference(getString(R.string.pref_compass_key));
            compassPref.setOnPreferenceChangeListener(this);
            //Other Settings
            Preference datatxPref = findPreference(getString(R.string.pref_http_key));
            datatxPref.setOnPreferenceChangeListener(this);   //Registering a listener for this item
            Preference serialBaudPref = findPreference(getString(R.string.pref_serial_baudrate_key));
            serialBaudPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            Preference portNumPref = findPreference(getString(R.string.pref_port_numb_key));
            portNumPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            Preference s1IdPref = findPreference(getString(R.string.pref_s1_id_key));
            s1IdPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            Preference s2IdPref = findPreference(getString(R.string.pref_s2_id_key));
            s2IdPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            //Table Settings
            Preference initTablePref = findPreference(getString(R.string.pref_init_table_key));
            initTablePref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            Preference tableNamePref = findPreference(getString(R.string.pref_table_name_key));
            tableNamePref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            //Tolerance Settings
            Preference toleranceThetaPref = findPreference(getString(R.string.pref_tolerance_theta_key));
            toleranceThetaPref.setOnPreferenceChangeListener(this);//Registering a listener for this item
            Preference tolerancePhiPref = findPreference(getString(R.string.pref_tolerance_phi_key));
            tolerancePhiPref.setOnPreferenceChangeListener(this);//Registering a listener for this item

        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //The following code in this method is for displaying the values of all the settings whenever
        //the app switches to the settings page
        String serverAddr = sharedPref.getString(getString(R.string.pref_http_key), getString(R.string.pref_http_default));  //Get the server Address
        findPreference(getString(R.string.pref_http_key)).setSummary(serverAddr);
        String baudRate = sharedPref.getString(getString(R.string.pref_serial_baudrate_key), getString(R.string.pref_serial_baudrate_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_serial_baudrate_key)).setSummary(baudRate);
        //JSON settings for GPS, RSS S1, RSS S2, Gyro, Mag, Compass
        //Boolean gpsBool = sharedPref.getBoolean(getString(R.string.pref_gps_changes_key), false);
        // Get port number
        //findPreference(getString(R.string.pref_port_numb_key)).setSummary(gpsBool);
        //New settings with port number, S1 id, S2 id
        String portNum = sharedPref.getString(getString(R.string.pref_port_numb_key), getString(R.string.pref_port_numb_default));  //Get port number
        findPreference(getString(R.string.pref_port_numb_key)).setSummary(portNum);
        String s1Id = sharedPref.getString(getString(R.string.pref_s1_id_key), getString(R.string.pref_s1_id_default));  //Get the S1 id
        findPreference(getString(R.string.pref_s1_id_key)).setSummary(s1Id);
        String s2Id = sharedPref.getString(getString(R.string.pref_s2_id_key), getString(R.string.pref_s2_id_default));  //Get the S2 id
        findPreference(getString(R.string.pref_http_key)).setSummary(s2Id);
        //Table settings
        //String tableInit = sharedPref.getString(getString(R.string.pref_init_table_key), getString(R.string.pref_init_table_default));
        // Get the Baud Rate of the Serial
        //findPreference(getString(R.string.pref_init_table_key)).setSummary(tableInit);
        String tableName = sharedPref.getString(getString(R.string.pref_table_name_key), getString(R.string.pref_table_name_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_table_name_key)).setSummary(tableName);
        //Tolerance settings
        String tolerTheta = sharedPref.getString(getString(R.string.pref_tolerance_theta_key), getString(R.string.pref_tolerance_theta_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_tolerance_theta_key)).setSummary(tolerTheta);
        String tolerPhi = sharedPref.getString(getString(R.string.pref_tolerance_phi_key), getString(R.string.pref_tolerance_phi_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_tolerance_phi_key)).setSummary(tolerPhi);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //This method is invoked whenever a change has been made to any of the settings items

        if(preference.getKey().equals(getString(R.string.pref_http_key))){  //If the server address is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);    //Setting the summary of an item
        } else if(preference.getKey().equals(getString(R.string.pref_serial_baudrate_key))) {  //If the baud rate is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_port_numb_key))) {  //If the port number is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_s1_id_key))) {  //If the S1 ID is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_s2_id_key))) {  //If the S2 ID is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        //} else if(preference.getKey().equals(getString(R.string.pref_init_table_key))) {  //If the initial table is changed
          //  String newValueStr = newValue.toString();
            //preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_table_name_key))) {  //If the table name is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_tolerance_theta_key))) {  //If the tolerance theta is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_tolerance_phi_key))) {  //If the tolerance phi is changed
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        }


        return true;
    }
}
