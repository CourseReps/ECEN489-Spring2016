/**
 * @file MainActivity.java
 *
 * @brief This is the only activity in the project
 *
 *  contains the java brains needed for switching between fragments to load onto its xml file
 **/

/**
 * Created by fanchaozhou on 2/22/16.
 */
package edu.tamu.ecen489s2016.project2;

import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

/**
 * @class MainActivity
 *
 * @brief Java class for handling switching between fragments
 */
public class MainActivity extends AppCompatActivity{

    //These three references are proved to be redundant
    private SettingsFragment settingsFrag;
    private MainFragment mainFrag;
    private AboutUsFragment aboutFrag;
    private MapsViewFragment mapsFrag;

    /**
     * @fn onCreate
     * @brief executed when the activity is created. This is the activity created upon launch as well.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing the action bar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // First we want to start with showing the data fragment
        if (savedInstanceState == null/*When the app has just started*/) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFrag = new MainFragment())  //Start a main fragment
                    .commit();
        }
    }

    /**
     * @fn onCreateOptionsMenu
     * @brief Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /**
     * @fn onOptionsSelected
     * @brief Used to load proper fragment that the user selects fromm the menu.
     * Handle action bar item clicks here. The action bar will automatically handle clicks on
     * the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Switching to the settings page
        if (id==R.id.action_settings) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);    //Pushing to the stack for BACK button to trace back to previous fragments
            transaction.replace(R.id.container, settingsFrag = new SettingsFragment());  //Start a settings fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).show(settingsFrag);//Setting the animation
            transaction.commit();

            return true;
        }
        //Switching to the about-us page
        else if(id==R.id.action_about_us){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, aboutFrag = new AboutUsFragment());  //Start an about-us fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE ).show(aboutFrag);
            transaction.commit();

            return true;
        }
        //Switching to the main page
        else if(id==R.id.action_data){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, mainFrag = new MainFragment());  //Start a main fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE ).show(mainFrag);
            transaction.commit();

            return true;
        }
        //Switching to Map page
        else if(id==R.id.action_map)
        {
            //Intent intent = new Intent(this, MapsActivity.class);
            //startActivity(intent);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, mapsFrag = new MapsViewFragment());     //Start a map fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE ).show(mapsFrag);
            transaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * @fn onBackPressed
     * @brief Popping the previous fragments out when the BACK button is pressed
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

}
