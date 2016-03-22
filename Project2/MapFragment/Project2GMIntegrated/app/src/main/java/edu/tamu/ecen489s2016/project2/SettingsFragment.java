/**
 * @file SettingsFragment.java
 *
 * @brief Reads in Preferences from all the toggle widgets in the Settings Fragment
 *
**/
package edu.tamu.ecen489s2016.project2;

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
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

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
