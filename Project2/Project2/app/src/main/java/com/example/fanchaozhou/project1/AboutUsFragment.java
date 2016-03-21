/**
 * @file AboutUsFragment.java
 *
 * @brief The brains behind the AboutUs Fragment.
 *
 * Contains AboutUsFragment class and ReadAboutUs class
 **/

/**
 * Created by fanchaozhou on 2/22/16.
 */
package com.example.fanchaozhou.project1;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @class AboutUsFragment
 *
 * @brief Used to inflate the fragment xml into the main Activity xml and start the reader.
 */
public class AboutUsFragment extends Fragment {

    /**
     * @fn onCreateView
     * @brief loads fragment_about_us
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if(savedInstanceState == null){
            rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        }

        return rootView;
    }


    /**
     * @fn onStarte
     * @brief create ReadAboutUs() ojbject and calls execute on it.
     */
    @Override
    public void onStart() {
        super.onStart();
        new ReadAboutUs().execute();
    }
    /**
     * @class ReadAboutUs
     *
     * @brief Reads the About Us information from a text file and prints to screen at the end.
     *
     */
    private class ReadAboutUs extends AsyncTask<Void, Void, String>{
        /**
         * @fn doInBackground
         * @brief reads into an InputStream
         */
        @Override
        protected String doInBackground(Void... params) {
            InputStream is = getActivity().getResources().openRawResource(R.raw.aboutus);
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line+'\n');
                }

            } catch(Exception e){
                System.out.println(e);
            }

            return sb.toString();
        }

        /**
         * @fn onPostExecute
         * @brief puts Input stream from previous into a textview
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView aboutUsView = new TextView(getActivity());
            aboutUsView.setText(s);
            ScrollView sv = (ScrollView)getActivity().findViewById(R.id.scrollView);
            sv.addView(aboutUsView);
        }
    }
}
