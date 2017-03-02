// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//          RF Signal Map
// ---------------------------------------------------------------------------------------------------------------------
/**
 * @file         AboutFragment.java
 * @brief        Project #3 - About Application Fragment
 **/
//  --------------------------------------------------------------------------------------------------------------------
//  Package Name
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.rfsignalmap;

//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import android.content.Context;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//----------------------------------------------------------------------------------------------------------------------
/** @class      AboutFragment
 *  @brief      About Fragment - Application Information
 */
public class AboutFragment extends Fragment {
    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onAttach
     * @brief   onAttach Event
     *
     *          Inputs: Context
     *          Return: none
     *          Event upon the fragment attaching to the activity
     */
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onCreate
     * @brief   onCreate Event
     *
     *          Inputs: Saved Instance State
     *          Return: none
     *          Event on the creation or recreation of this fragment.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onCreateView
     * @brief   onCreateView Event
     *
     *          Inputs: Inflater, Container, and Saved Instance State
     *          Return: View
     *          Create View and Fragment Resources
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, parent, false);
    }

/*    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @fn      onAttach
     * @brief   onAttach Event
     *
     *          Inputs: Context
     *          Return: none
     *          Event upon the fragment attaching to the activity
     */
/*    public void onViewCreated(View view, Bundle savedInstanceState) {
    //   ListView lv = (ListView) view.findViewById(R.id.lvSome);
    //    lv.setAdapter(adapter);
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
*/
}
