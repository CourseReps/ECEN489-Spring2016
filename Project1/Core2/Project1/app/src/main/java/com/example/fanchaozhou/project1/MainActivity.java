package com.example.fanchaozhou.project1;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Fanchao Zhou on 2/21/2016.
 */

public class MainActivity extends AppCompatActivity {

    private SettingsFragment settingsFrag;
    private MainFragment mainFrag;
    private AboutUsFragment aboutFrag;
    private Fragment curFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFrag = new MainFragment();
        settingsFrag = new SettingsFragment();
        aboutFrag = new AboutUsFragment();

        if (savedInstanceState == null/*When the app has just started*/) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFrag)  //Start a main fragment
                    .commit();
            curFrag = mainFrag;
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

        if(mainFrag.isVisible()){
            curFrag = mainFrag;
        } else if(settingsFrag.isVisible()){
            curFrag = settingsFrag;
        } else if(aboutFrag.isVisible()){
            curFrag = aboutFrag;
        }

        if (id==R.id.action_settings && curFrag!=settingsFrag) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(curFrag);
            transaction.replace(R.id.container, settingsFrag = new SettingsFragment());  //Start a settings fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).show(settingsFrag);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        } else if(id==R.id.action_about_us && curFrag!=aboutFrag){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(curFrag);
            transaction.replace(R.id.container, aboutFrag = new AboutUsFragment());  //Start an about-us fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE ).show(aboutFrag);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        } else if(id==R.id.action_data && curFrag!=mainFrag){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(curFrag);
            transaction.replace(R.id.container, mainFrag = new MainFragment());  //Start a main fragment
            transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE ).show(mainFrag);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
