package edu.tamu.testoauthlogin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.auth.oauth2.TokenResponse;


import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import com.google.api.services.fusiontables.Fusiontables.Query.Sql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static String CLIENT_ID = "94516972404-henq84n3gqg15a71l9uo2mj99jsmaaag.apps.googleusercontent.com";
    //Use your own client id
    private static String CLIENT_SECRET ="QL8-4PAdwMNFnjKZeNl-nSOl";
    //Use your own client secret
    private static String REDIRECT_URI="http://localhost";
    private static String GRANT_TYPE="authorization_code";
    private static String TOKEN_URL ="https://accounts.google.com/o/oauth2/token";
    private static String OAUTH_URL ="https://accounts.google.com/o/oauth2/auth";
    private static String OAUTH_SCOPE="https://www.googleapis.com/auth/fusiontables";
    //Change the Scope as you need
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;

    private TokenResponse Token;

    private static Fusiontables FusionTables;                           // Global instance of the Fusion Tables

    private static HttpTransport HTTP_TRANSPORT;                        // Global instance of the HTTP transport
    // Global instance of the JSON factory.
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    //private static final java.io.File DATA_STORE_DIR = new java.io.File(getFilesDir(), CREDENTIALS_DIRECTORY);


    private static final String APPLICATION_NAME = "ECEN689RFFields";   // Set the Application Name


    // -----------------------------------------------------------------------------------------------------------------
    // Create new instances of HTTP_TRANSPORT
    static                                                              //
    {                                                                   // and DATA_STORE_FACTORY
        try                                                             //
        {                                                               //
            HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            //DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            // DATA_STORE_FACTORY = new AbstractDataStoreFactory();
        }                                                               //
        catch (Throwable t)                                             //
        {                                                               //
            t.printStackTrace();                                        //
            System.exit(1);                                             //
        }                                                               //
    }                                                                   //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                auth_dialog = new Dialog(MainActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(OAUTH_URL+"?redirect_uri="+REDIRECT_URI+"&response_type=code&client_id="+CLIENT_ID+"&scope="+OAUTH_SCOPE);
                web.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);

                    }
                    String authCode;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.contains("?code=") && authComplete != true) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("", "CODE : " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            MainActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();
                            new TokenGet().execute();
                            Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
                        }else if(url.contains("error=access_denied")){
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Test OAuth 2.0 For ECEN 689 Fusion Tables");
                auth_dialog.setCancelable(true);
            }
        });



    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Contacting Google ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null){

                try {

                    String tok = json.getString("access_token");
                    String expire = json.getString("expires_in");
                    String refresh = json.getString("refresh_token");

                    Token = new TokenResponse().setAccessToken(tok).setRefreshToken(refresh);


                    Log.d("Token Access", tok);
                    Log.d("Expire", expire);
                    Log.d("Refresh", refresh);
                    auth.setText("Authenticated");
                    Access.setText("Access Token:"+tok+"nExpires:"+expire+"nRefresh Token:"+refresh);

                    ConnectToDatabase();



                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AuthorizeTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private boolean connected = false;

        protected String doInBackground(String... data)
        {
            try
            {
                AssetManager assetManager = getAssets();
                InputStream in =  assetManager.open("client_secret_test.json");
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


                GoogleCredential credential =  new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY).setClientSecrets(clientSecrets).build();
                credential.setFromTokenResponse(Token);
                System.out.println("Credential: " + credential.toString());

                // Build Fusion Table
                // Set up global FusionTables instance
                FusionTables = new Fusiontables.Builder(HTTP_TRANSPORT,
                        JSON_FACTORY,
                        credential).setApplicationName(APPLICATION_NAME).build();
                connected = true;
                System.out.println("FT DONE!");

                // Get a new Lat/Long randomly around the EIC area
                LatLong newpos = new LatLong(359.99 * Math.random(), 650.0 * Math.random(),  30.618651, -96.341498);

                RFData testmember = new RFData();                               // Create test RF data
                testmember.XbeeID = 456;                                        // Fill test with dummy data
                testmember.DeviceID = 1234;
                testmember.Latitude = (float)newpos.Latitude;
                testmember.Longitude = (float)newpos.Longitude;
                testmember.RSSI = (float)(100.0 * Math.random());
                testmember.Yaw = (float)(359.99 * Math.random());
                testmember.Pitch = (float)(359.99 * Math.random());
                testmember.Roll = (float)(359.99 * Math.random());
                testmember.SampleDate = new Date();

                System.out.println("Add data...");
                if (AddNewEntry(testmember) == true)
                {
                    System.out.println("Add success!");
                }


                return "Done";
            }
            catch (Exception e)
            {
                System.out.println("Err: " + e.getMessage());
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String result)
        {
            System.out.println("DONE!");
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    //	----------------------------------------------------------------------------------------------------------------
    //       Method:    ConnectToDatabase
    //       Inputs:    none
    //      Outputs:    Success = TRUE / Failure = FALSE
    //  Description:    Establishes a connection to the database
    //	----------------------------------------------------------------------------------------------------------------
    public boolean ConnectToDatabase()
    {
        boolean status = false;                                         // Return status (success / failure)
        try
        {
            System.out.println("Connect To Database");
            new AuthorizeTask().execute("Test");

            status = true;                                          // Set flag to true
        }                                                           //
        catch (Throwable t)                                         //
        {                                                           //
            t.printStackTrace();                                    //
        }                                                               //
        return status;                                                  // Return status
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     GetTableId
    //      Inputs:	    none
    //     Outputs:	    none
    // Description:     Get Table ID from Table Nam
    //	----------------------------------------------------------------------------------------------------------------
    private static String GetTableId(String tableName) throws IOException
    {
        String tid = null;                                              // Set default return
        try {
            System.out.println("Get Table ID: " + tableName);               // Print header
            // Get the table list


            Fusiontables.Table.List listTables = FusionTables.table().list();

            TableList tablelist = listTables.execute();                    //
            System.out.println("Got table list");

            // If there are no tables, print that info
            if (tablelist.getItems() == null || tablelist.getItems().isEmpty())
            {                                                               //
                System.out.println("No tables found!");                     //
            }                                                               //
            else                                                            // Else, loop through tables, find match
            {                                                               // If it matches then save the table ID
                System.out.println("Check tables");


                for (Table table : tablelist.getItems())                    //
                {                                                           //
                    if (table.getName().equals(tableName)) tid = table.getTableId();
                }                                                           // Printout the results of that
                System.out.println(tableName + " - tableId: " + tid);       //
            }                                                               //
        }
        catch (Exception e)
        {
            System.out.println("e= " + e.getMessage());                     //
        }
        return tid;                                                     // Return the table ID
    }


    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     AddNewEntry
    //      Inputs:	    RF Data Entry (JSON)
    //     Outputs:	    Success = TRUE / Failure = FALSE
    // Description:     Insert new data to table  (executes SQL command)
    //	----------------------------------------------------------------------------------------------------------------
    public boolean AddNewEntry(RFData RFMember)
    {
        boolean status = false;                                         // Return status (success / failure)

        try                                                             // Try to get JSON, and save data to database
        {                                                               //
            if (RFMember.XbeeID != -1)                                  // If not default then save data
            {
                // Print debug information to port
                System.out.println("Insert New RF Data into Table - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI);

                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sql_string;                                      // Build up SQL string
                // Add to fusion table
                String tableId = GetTableId("RF Field Data");       // Get the "Customer Data" Table
                sql_string = "INSERT INTO " + tableId + " (";       // Insert SQL statement, Table: Table ID
                sql_string += "XbeeID,";                            // Field: intXbeeID
                sql_string += "DeviceID,";                          // Field: intDeviceID
                sql_string += "RSSI,";                              // Field: fltRSSI
                sql_string += "Location,";                          // Field: fltLatitude
                sql_string += "Longitude,";                         // Field: fltLongitude
                sql_string += "Yaw,";                               // Field: fltYaw
                sql_string += "Pitch,";                             // Field: fltPitch
                sql_string += "Roll,";                              // Field: fltRoll
                sql_string += "SampleDate) ";                       // Field: dtSampleDate
                sql_string += "VALUES (";                           // Values indetifier
                sql_string += RFMember.XbeeID + ",";                // Value: XbeeID
                sql_string += RFMember.DeviceID + ",";              // Value: DeviceID
                sql_string += RFMember.RSSI + ",";                  // Value: RSSI
                sql_string += RFMember.Latitude + ",";              // Value: Latitude
                sql_string += RFMember.Longitude + ",";             // Value: Longitude
                sql_string += RFMember.Yaw + ",";                   // Value: Yaw
                sql_string += RFMember.Pitch + ",";                 // Value: Pitch
                sql_string += RFMember.Roll + ",";                  // Value: Roll
                sql_string += "'" + ft.format(RFMember.SampleDate) + "')";
                Sql sql = FusionTables.query().sql(sql_string);     // Build Fusion Query
                // Try and execute the SQL command
                try                                                 //
                {                                                   //
                    sql.executeAndDownloadTo(System.out);           // Execute command, stream to the system.out
                    status = true;                                  //
                }                                                   //
                catch (IllegalArgumentException e)                  //
                {                                                   //
                }                                                   //
            }                                                           //
            else                                                        //
            {                                                           //
                System.err.println("Err AddNewEntry: Invalid JSON data");   // Print the exception data and exit
                status = false;                                         // Failure, invalid JSON or data
            }
        }                                                               //
        catch  ( Exception e )                                          // Exception processing:
        {                                                               //
            System.err.println("Err AddNewEntry: " + e.getMessage() );      // Print the exception data and exit
            status = false;                                             // Failure
        }                                                               //
        return status;                                                  // Return status
    }

}
