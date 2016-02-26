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
 * Created by Fanchao Zhou on 2/23/2016.
 */
public class AboutUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if(savedInstanceState == null){
            rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ReadAboutUs().execute();
    }

    private class ReadAboutUs extends AsyncTask<Void, Void, String>{
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
