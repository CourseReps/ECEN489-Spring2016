/**
 * @file AlignmentFragement.java
 *
 * @brief Used to show and calibrate the alignment of the phone
 *
 **/

/**
 * Created by Kyle on 2/22/16.
 */
package edu.tamu.ecen489s2016.project2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @class AlignmentFragement
 *
 * @brief The brains behind the alignment fragment
 */
public class AlignmentFragment extends Fragment{

    /**
     * @fn onCreateView
     * @brief loads the alignment xml as a fragment into the MainFragment
     *
     * A Fragment inside another fragment may not be wise or Androidesque but it works for now.
     * Consider creating another activity.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.alignment_fragment, container, false);
        return v;
    }
}
