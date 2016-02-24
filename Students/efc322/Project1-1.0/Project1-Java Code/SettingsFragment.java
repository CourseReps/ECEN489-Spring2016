package com.example.fanchaozhou.project1;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fanchao Zhou on 2/21/2016.
 */
public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

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
            Preference datatxPref = findPreference(getString(R.string.pref_datatx_key));
            datatxPref.setOnPreferenceChangeListener(this);
        }

        return rootView;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String newValueStr = newValue.toString();

        //The CheckBox for data transmission has be clicked on
        if(preference.getKey().equals(getString(R.string.pref_datatx_key))){
            if(newValueStr == "true"){
                preference.setSummary("On");
                //TODO: Add Code to OPEN the DATA TRANSMISSION
            }else{
                preference.setSummary("Off");
                //TODO: Add Code to PAUSE the DATA TRANSMISSION
            }
        }

        return true;
    }
}
