// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         MapsActivity.java
 * @brief        Project #2 - Fusion Map of RF Data
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package com.example.fanchaozhou.project1;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//----------------------------------------------------------------------------------------------------------------------

/** @class      MapsViewFragment
 *  @brief      Maps View Fragment for Application Project
 */
public class MapsViewFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap mMap;                         /// Google Map Instance
    private HeatmapTileProvider mProvider;          /// Heatmap Provider
    private TileOverlay mOverlay;                   /// Tile Overlay
    private LocationManager locationManager;        /// Location Manager
    private MapFragment mapFragment;                /// Map Fragment

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onCreate Event
     *
     *           Inputs: savedInstanceState
     *           Return: none
     *           On Create
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onCreate Event
     *
     *           Inputs: savedInstanceState
     *           Return: none
     *           Create View and Fragment Resources
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        Bundle bdl = getArguments();

        /// If map fragment is null set to new instance and get the callback
        if (mapFragment == null)
        {
            mapFragment = MapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        // Get the child tranaction and replace the map
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, mapFragment).commit();

        // Return the view
        return v;
    }


    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    onMapReady
     *
     *           Inputs: googleMap
     *           Return: none
     *           Manipulates the map once available.
     *           This callback is triggered when the map is ready to be used.

     *           If Google Play services is not installed on the device, the user will be prompted to install
     *           it inside the SupportMapFragment. This method will only be triggered once the user has
     *           installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;                                               /// Get the map now that it is ready

        /// Add a marker in EIC and move the camera
        /// We will need to hack this to work with other lat/long values to be displayed
        LatLng eic = new LatLng(30.618708, -96.341558);
        mMap.addMarker(new MarkerOptions().position(eic).title("EIC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eic, 19));

        // Calls function to update the map on the container
        UpdateMap((float) eic.latitude, (float) eic.longitude, 1000F);
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    UpdateMap
     *
     *           Inputs: Center location (Lat/Long), Radius to get data from reference
     *           Return: none
     *           Manipulates the map once available and gets the data from the database
     */
    public void UpdateMap(float RefLatitude, float RefLongitude, float Radius)
    {
        LatLng ref_location = new LatLng(RefLatitude, RefLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ref_location, 19));
        GetRFData UpdateMapTask =  new GetRFData();
        UpdateMapTask.RefLatitude = RefLatitude;
        UpdateMapTask.RefLongitude = RefLongitude;
        UpdateMapTask.Radius = Radius;
        UpdateMapTask.execute("");
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    GetRFData
     *
     *           AsyncTask that gets data from MySQL datbase and displays it on map
     */
    private class GetRFData extends AsyncTask<String, Void, ArrayList>
    {
        private Exception exception;
        public float RefLatitude = 30.618708F;
        public float RefLongitude = -96.341558F;
        public float Radius = 1000.0F;

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @brief    doInBackground
         *
         *           Gets the data from the MySQL server then calls onPostExecute
         */
        protected ArrayList doInBackground(String... parameters)
        {
            try
            {
                float StartLatitude, StartLongitude, EndLatitude, EndLongitude;

                /// Get the North, South, East, and West Lat/Long locations from the center location and radius given
                LatLong mLatLong_N = new LatLong(0, Radius, RefLatitude, RefLongitude);
                LatLong mLatLong_S = new LatLong(180, Radius, RefLatitude, RefLongitude);
                LatLong mLatLong_E = new LatLong(90, Radius, RefLatitude, RefLongitude);
                LatLong mLatLong_W = new LatLong(270, Radius, RefLatitude, RefLongitude);

                /// Assign starting and ending values based upon the computed values
                /// Making sure that start is the least and end is the greatest
                if (mLatLong_S.Latitude < mLatLong_N.Latitude)
                {
                    StartLatitude = (float)mLatLong_S.Latitude;
                    EndLatitude = (float)mLatLong_N.Latitude;
                }
                else
                {
                    StartLatitude = (float)mLatLong_N.Latitude;
                    EndLatitude = (float)mLatLong_S.Latitude;
                }

                if (mLatLong_E.Longitude < mLatLong_W.Longitude)
                {
                    StartLongitude = (float)mLatLong_E.Longitude;
                    EndLongitude = (float)mLatLong_W.Longitude;
                }
                else
                {
                    StartLongitude = (float)mLatLong_W.Longitude;
                    EndLongitude = (float)mLatLong_E.Longitude;
                }

                /// Pull from database the data that matches this range
                RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();

                /// Connect to test server (for now), if not connected return null
                if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com") == true)
                {
                    /// Pull data from database, return the results
                    ArrayList records = RFFieldDatabase.ListDataByGeoArea(StartLatitude, StartLongitude, EndLatitude, EndLongitude);

                    /// Return the records
                    return records;
                }
                else return null;

            }
            catch (Exception e)
            {
                /// Exception processing, display error and then return null
                System.out.println("Err: " + e.getMessage());
                this.exception = e;
                return null;
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @brief    onPostExecute
         *
         *           Process and display the resulting records
         */
        protected void onPostExecute(ArrayList records)
        {
            try
            {
                List<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
                Iterator itr = records.iterator();                              /// Build an Iterator for the Array List
                while (itr.hasNext())                                            /// Loop through the list
                {                                                               /// Entries are RF Data Members
                    Object element = itr.next();                                /// Get the next object
                    RFData RFMember = (RFData) element;                          /// Cast the object to the data member

                    // Print debug information to port
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + ", Lat: " + RFMember.Latitude + ", Long: " + RFMember.Longitude + " Date/Time: " + ft.format(RFMember.SampleDate));

                    /// Add the RF data lat/long to the map
                    LatLng newpos = new LatLng(RFMember.Latitude, RFMember.Longitude);
                    WeightedLatLng newWLL = new WeightedLatLng(newpos, RFMember.RSSI);
                    list.add(newWLL);
                }

                /// Clear the map
                mMap.clear();

                /// Only add to the map if there is data to be added
                if (list.isEmpty() == false)
                {
                    /// Create a heat map tile provider, passing it the latlngs of the rf data.
                    mProvider = new HeatmapTileProvider.Builder()
                            .weightedData(list)
                            .build();

                    /// Add overlay
                    mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                }
            }
            catch (Exception e)
            {
                /// Exception processing, display error
                System.out.println("Err: " + e.getMessage());
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    SaveRFData
     *
     *           AsyncTask that gets data from MySQL datbase and displays it on map
     */
    private class SaveRFData extends AsyncTask<String, Void, String>
    {
        private Exception exception;
        public RFData RFMember;
        public boolean AddComplete = false;
        public boolean AddErr = false;

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @brief    doInBackground
         *
         *           Gets the data from the MySQL server then calls onPostExecute
         */
        protected String doInBackground(String... parameters)
        {
            try
            {
                AddComplete = false;
                if (RFMember != null)
                {
                    /// Pull from database the data that matches this range
                    RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();

                    /// Connect to test server (for now), if not connected return null
                    if (RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com") == true) {
                        /// Store data to database, return the results
                        boolean status = RFFieldDatabase.AddNewEntry(RFMember);
                        if (status) return "Success";
                        else return "Failed to add";
                    }
                    else return "Not Connected";
                }
                else return "Null Data";

            }
            catch (Exception e)
            {
                /// Exception processing, display error and then return null
                System.out.println("Err: " + e.getMessage());
                this.exception = e;
                return e.getMessage();
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * @brief    onPostExecute
         *
         *           Process and display the resulting records
         */
        protected void onPostExecute(String result)
        {
            if (result == "Success") AddErr = false;
            else AddErr = true;
            AddComplete = true;
        }
    }
}
