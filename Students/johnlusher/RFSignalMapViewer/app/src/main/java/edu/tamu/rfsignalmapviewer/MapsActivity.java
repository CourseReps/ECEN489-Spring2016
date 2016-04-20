package edu.tamu.rfsignalmapviewer;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;          /// Heatmap Provider
    private TileOverlay mOverlay;                   /// Tile Overlay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /// Add a marker in EIC and move the camera
        /// We will need to hack this to work with other lat/long values to be displayed
        LatLng eic = new LatLng(30.618708, -96.341558);
        mMap.addMarker(new MarkerOptions().position(eic).title("EIC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eic, 19));
        mMap.setOnMapClickListener(this);

        // Calls function to update the map on the container
        UpdateMap((float) eic.latitude, (float) eic.longitude, 1000F);
    }

    //@Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @brief    OnMapClick
     *
     *           Inputs: Position (Lat/Long)
     *           Return: none
     *           Updates data from database upon map click
     *           This callback is triggered when the map is clicked.
     */
    public void onMapClick(LatLng position)
    {
        UpdateMap((float) position.latitude, (float) position.longitude, 1000F);
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
}
