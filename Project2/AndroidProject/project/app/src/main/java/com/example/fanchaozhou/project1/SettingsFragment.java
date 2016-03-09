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
            Preference datatxPref = findPreference(getString(R.string.pref_http_key));
            datatxPref.setOnPreferenceChangeListener(this);
            Preference serialBaudPref = findPreference(getString(R.string.pref_serial_baudrate_key));
            serialBaudPref.setOnPreferenceChangeListener(this);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String serverAddr = sharedPref.getString(getString(R.string.pref_http_key), getString(R.string.pref_http_default));  //Get the server Address
        findPreference(getString(R.string.pref_http_key)).setSummary(serverAddr);
        String baudRate = sharedPref.getString(getString(R.string.pref_serial_baudrate_key), getString(R.string.pref_serial_baudrate_default));  //Get the Baud Rate of the Serial
        findPreference(getString(R.string.pref_serial_baudrate_key)).setSummary(baudRate);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if(preference.getKey().equals(getString(R.string.pref_http_key))){
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        } else if(preference.getKey().equals(getString(R.string.pref_serial_baudrate_key))) {
            String newValueStr = newValue.toString();
            preference.setSummary(newValueStr);
        }

        return true;
    }
}
