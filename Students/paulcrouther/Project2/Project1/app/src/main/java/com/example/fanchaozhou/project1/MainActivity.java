package com.example.fanchaozhou.project1;

import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Fanchao Zhou on 2/21/2016.
 */

public class MainActivity extends AppCompatActivity{

    //These three references are proved to be redundant
    private SettingsFragment settingsFrag;
    private MainFragment mainFrag;
    private AboutUsFragment aboutFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing the action bar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null/*When the app has just started*/) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFrag = new MainFragment())  //Start a main fragment
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {  //Popping the previous fragments out when the BACK button is pressed
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

}
