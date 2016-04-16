// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//          RF Signal Map
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         SettingsFragment.java
 * @brief        RF Signal Main Activity - Application Settings Fragment
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.rfsignalmap;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//----------------------------------------------------------------------------------------------------------------------

/** @class      SettingsFragment
 *  @brief      Settings Fragment - Application Information
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
        Preference datatxPref = findPreference(getString(R.string.pref_server_key));
        datatxPref.setOnPreferenceChangeListener(this);   //Registering a listener for this item
    }


    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onCreateView Event
     *
     *           Inputs: Inflater, Container, and Saved Instance State
     *           Return: none
     *           Create View and Fragment Resources
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return  super.onCreateView(inflater, parent, savedInstanceState);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(savedInstanceState == null){
            Preference datatxPref = findPreference(getString(R.string.pref_server_key));
            datatxPref.setOnPreferenceChangeListener(this);   //Registering a listener for this item
        }
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @fn onStart
     * @brief Get server settings and display them
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        try {

            String serverAddr = sharedPref.getString(getString(R.string.pref_server_key), getString(R.string.pref_server_default));
            findPreference(getString(R.string.pref_server_key)).setSummary(serverAddr);
        }
        catch (Exception e)
        {

        }
    }

    /**
     * @fn onPreferenceChange
     * @brief This method is invoked whenever a change has been made to any of the settings items
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // If the host address is changed
        try {
            if (preference.getKey().equals(getString(R.string.pref_server_key))) {
                String newValueStr = newValue.toString();
                preference.setSummary(newValueStr);
            }
        }
        catch (Exception e)
        {

        }

        return true;
    }
}
