// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//          RF Signal Map
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         MainActivity.java
 * @brief        RF Signal Main Activity
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.rfsignalmap;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

//----------------------------------------------------------------------------------------------------------------------
/** @class      MainActivity
 *  @brief      Main Activity Application Project
 */
public class MainActivity extends AppCompatActivity {

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onCreate Event
     *
     *           Inputs: savedInstanceState
     *           Return: none
     *           On Create
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, new MainActivityFragment());
            transaction.commit();
        }
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onCreateOptionsMenu Event
     *
     *           Inputs: Menu
     *           Return: true (boolean)
     *           Inflate the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onOptionsItemSelected Event
     *
     *           Inputs: Menu Tem
     *           Return: true/false (Item is Selected)
     *           Perform function based upon menu item selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Execute fragment based upon selection
        if (id == R.id.action_rfview) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, new MainActivityFragment());
            transaction.commit();
            return true;
        }
        else if (id == R.id.action_settings) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, new SettingsFragment());
            transaction.commit();
            return true;
        }
        else if (id == R.id.action_about) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, new AboutFragment());
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
