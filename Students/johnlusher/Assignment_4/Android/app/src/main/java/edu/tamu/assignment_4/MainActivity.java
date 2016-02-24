// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		MainActivity.java
// Version:			1.0.0
// Date:			February 16, 2016
// Description:	    Assignmen #4 - Android App to Tomcat Server
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           February 16, 2016  	     Initial Release
//  --------------------------------------------------------------------------------------------------------------------


//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.assignment_4;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.app.AlertDialog.Builder;
import android.view.View.OnClickListener;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    MainActivity
//  Description:	MainActivity class for project
//  --------------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements OnClickListener
{
                                                                        // ---------------------------------------------
    EditText editURL, editSend;                                         // Edit fields
    Button buttonPush, buttonGet;                                       // Push and Get Buttons
    TextView textStatus;                                                // Status text
                                                                        // ---------------------------------------------

    @Override
    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     onCreate (OVERRIDE)
    //      Inputs:	    savedInstanceState
    //     Outputs:	    none
    //  Description:    onCreate Event
    //	----------------------------------------------------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState)
    {
                                                                        // ---------------------------------------------
        super.onCreate(savedInstanceState);                             //
        setContentView(R.layout.activity_main);                         // Content View - Main Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);         // Tool bar
        setSupportActionBar(toolbar);                                   // Set Action Bar - Toolbar
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
                                                                        // Setup Buttons and add onClick Listener
        buttonPush=(Button)findViewById(R.id.buttonPush);               //
        buttonPush.setOnClickListener(this);                            //
        buttonGet=(Button)findViewById(R.id.buttonGet);                 //
        buttonGet.setOnClickListener(this);                             //
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        editURL=(EditText)findViewById(R.id.editURL);                   // Edit Text Boxes
        editSend=(EditText)findViewById(R.id.editSend);                 //
                                                                        // ---------------------------------------------

                                                                        // ---------------------------------------------
        textStatus = (TextView)findViewById(R.id.textStatus);           // Text View
                                                                        // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    onClick
    //       Inputs:    View
    //      Outputs:	none
    //  Description:    onClick Event - Process Button Events
    //	----------------------------------------------------------------------------------------------------------------
    public void onClick(View view)
    {
                                                                        // ---------------------------------------------
        if (view == buttonPush)                                         // Process Push button
        {                                                               //
            if (editURL.getText().toString().trim().length() == 0)      // Verify all data is present (i.e. there is data)
            {                                                           //
                showMessage("Error", "Please enter host URL");          // If not then alert user
            }                                                           //
            else                                                        //
            {                                                           //
                String url = editURL.getText().toString();              // Get URL
                String strData = editSend.getText().toString();         // Get Data
                SendHttpRequestTask t = new SendHttpRequestTask();      //
                String[] params = new String[]{url, strData};           //
                t.execute(params);                                      //
            }                                                           //
        }                                                               //
        else if (view == buttonGet)                                     // Process Get button
        {                                                               //
            String url = editURL.getText().toString();                  // Get URL
            SendHttpGetRequestTask t = new SendHttpGetRequestTask();    //
            String[] params = new String[]{url};                        //
            t.execute(params);                                          //
        }                                                               // ---------------------------------------------
    }

    //	----------------------------------------------------------------------------------------------------------------
    //        Class:    SendHttpRequestTask
    //  Description:    Send HTTP Request Class
    //	----------------------------------------------------------------------------------------------------------------
    private class SendHttpRequestTask extends AsyncTask<String, Void, String>
    {
        @Override
        // Background task: Send HTTP Post Request
        protected String doInBackground(String... params)
        {
            String url = params[0];
            String strData = params[1];

            String data = sendHttpRequest(url, strData);
            return data;
        }

        @Override
        // After Post: Display results to debug port, inform user of post being completed
        protected void onPostExecute(String result)
        {
            System.out.println("Result ["+result+"]");
            textStatus.setText("Post Completed!");
        }
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    sendHttpRequest
    //       Inputs:    url, name
    //      Outputs:	string output
    //  Description:    HTTP Request
    //	----------------------------------------------------------------------------------------------------------------
    private String sendHttpRequest(String url, String strData)
    {
                                                                        // ---------------------------------------------
        StringBuffer buffer = new StringBuffer();                       // Build a new string buffer
        try                                                             // Trap any IO errors
        {                                                               // Post to the debug port
            System.out.println("URL [" + url + "] - Posted Data [" + strData + "]");
                                                                        // Build URL connection
            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");                               // We are posting data
            con.setDoInput(true);                                       //
            con.setDoOutput(true);                                      //
            con.connect();                                              // Connect, send posted data to servelet
            con.getOutputStream().write(("posteddata=" + strData).getBytes());
            InputStream is = con.getInputStream();                      // Get the response
            byte[] b = new byte[1024];                                  //
            while ( is.read(b) != -1) buffer.append(new String(b));     //
            con.disconnect();                                           //
        }                                                               //
        catch(Throwable t)                                              // On error print stack trace
        {                                                               //
            t.printStackTrace();                                        //
        }                                                               // ---------------------------------------------

        return buffer.toString();
    }

    //	----------------------------------------------------------------------------------------------------------------
    //        Class:    SendHttpGetRequestTask
    //  Description:    Send HTTP Get Request Class
    //	----------------------------------------------------------------------------------------------------------------
    private class SendHttpGetRequestTask extends AsyncTask<String, Void, String>
    {
        @Override
        // Background task: Send HTTP Post Request
        protected String doInBackground(String... params)
        {
            String url = params[0];
            String data = sendHttpGetRequest(url);
            return data;
        }

        @Override
        // After Post: Display results to debug port, inform user of post being completed
        protected void onPostExecute(String result)
        {
            System.out.println("Result ["+result+"]");
            textStatus.setText("Get Completed!");
        }
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    sendHttpRequest
    //       Inputs:    url, name
    //      Outputs:	string output
    //  Description:    HTTP Request
    //	----------------------------------------------------------------------------------------------------------------
    private String sendHttpGetRequest(String url)
    {
                                                                        // ---------------------------------------------
        StringBuffer buffer = new StringBuffer();                       // Build a new string buffer
        try                                                             // Trap any IO errors
        {                                                               // Post to the debug port
            System.out.println("URL [" + url + "]");                    // Build URL connection
            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");                                // We are getting data
            con.setDoInput(true);                                       //
            con.connect();                                              // Connect, send posted data to servelet
            InputStream is = con.getInputStream();                      // Get the response
            byte[] b = new byte[1024];                                  //
            while ( is.read(b) != -1) buffer.append(new String(b));     //
            con.disconnect();                                           //
        }                                                               //
        catch(Throwable t)                                              // On error print stack trace
        {                                                               //
            t.printStackTrace();                                        //
        }                                                               // ---------------------------------------------

        return buffer.toString();
    }

    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    showMessage
    //       Inputs:    Title, and Message
    //      Outputs:	none
    //  Description:    Show user notification box with data
    //	----------------------------------------------------------------------------------------------------------------
    public void showMessage(String title, String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
