package com.example.fanchaozhou.project1;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Fanchao Zhou on 2/22/2016.
 */
public class MainFragment extends Fragment {

    private static ArrayList<String> dataList = new ArrayList<>();
    private static ArrayAdapter<String> dataListAdaptor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            dataListAdaptor = new ArrayAdapter<String>(
                    getActivity(),                    //The Current Parent Activity
                    R.layout.single_record,           //The .xml file that contains the textview
                    R.id.list_item_record,            //The id of the textview
                    dataList                          //The source of the data
            );
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        dataListAdaptor.notifyDataSetChanged();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        Boolean isDataTx = sharedPref.
                getBoolean(getString(R.string.pref_datatx_key)
                        , getString(R.string.pref_datatx_default).equals("false") ? false : true);


        ImageView iv = (ImageView)getActivity().findViewById(R.id.imageView);
        if(isDataTx == true){
            Drawable datatxIcon = getResources().getDrawable(R.drawable.data_tx);
            iv.setImageDrawable(datatxIcon);
        }else{
            iv.setImageBitmap(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null){
            ListView list = (ListView)rootView.findViewById(R.id.list);  //Find the id of the target ListView
            list.setAdapter(dataListAdaptor);                            //Bind the adaptor to the ListView

            final Button button_refresh = (Button)rootView.findViewById(R.id.button_refresh);
            button_refresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO:Add code for DATA COLLECTION here

                    float[] rotate = new float[ 9 ];
                    float[] values = new float[ 3 ];
                    SensorManager sensorManager;
                    sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
                    sensorManager.getOrientation(rotate, values);

                    DataFunctions dataFunc = new DataFunctions(getActivity());
                    ArrayList<String> data = dataFunc.pulldata();
                    dataFunc.pushtodb();
                    dataList.add(0,
                            "Transmitter ID: "+data.get(0)+"\n"+
                            "Receiver ID: "+data.get(2)+"\n"+
                            "TimeStamp: "+data.get(5)+"\n"+
                            "RSSI: "+data.get(1)+"\n"+
                            "Orientation: "+Float.toString(values[0]) + Float.toString(values[1]) + Float.toString(values[2])+"\n"+
                            "Location: "+data.get(4)
                    );
                    /*
                    dataList.add(0,
                            "RSSI: " + (new Random()).nextFloat() + "dBm\n" +
                                    "Orientation: xxx\n" +
                                    "Location: xxx\n" +
                                    "TX ID: xxx\n" +
                                    "RX ID: xxx\n" +
                                    "Timestamp: xxx\n"
                    );*/
                    dataListAdaptor.notifyDataSetChanged();
                }
            });
            final Button button_clear_all = (Button)rootView.findViewById(R.id.button_clear_all);
            button_clear_all.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dataList.clear();
                    dataListAdaptor.notifyDataSetChanged();
                }
            });
        }

        return rootView;
    }
}
