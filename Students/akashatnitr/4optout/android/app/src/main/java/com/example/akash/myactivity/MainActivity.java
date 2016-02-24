package com.example.akash.myactivity;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
    Button button;
    Button getButton;
    TextView outputText;
    EditText editText;
    EditText editTextIP;
    String responseServer;
    String   msgTobeSent;
    public   String URL = "http://10.202.109.31:8080/message/message";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        button.setOnClickListener(this);
        getButton.setOnClickListener(this);
    }

    private void findViewsById() {
        button = (Button) findViewById(R.id.button);
        outputText = (TextView) findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        editTextIP = (EditText)findViewById(R.id.editText2);
        getButton = (Button)findViewById(R.id.button2);

    }

    public void onClick(View view) {
        outputText.setText("");
        GetXMLTask task = new GetXMLTask();
        msgTobeSent = editText.getText().toString();

       if(view.getId() == R.id.button) {

           if (editTextIP.getText().toString().length() > 4) {
               String tmp = editTextIP.getText().toString();
               URL = "http://" + tmp + ":8080/message/message";
           }


           //POst
           AsyncT asyncT = new AsyncT();
           asyncT.execute();
           //POst

       }
        if(view.getId() == R.id.button2) {
            String tmpURL = URL+"?firstname="+msgTobeSent;
             task.execute(new String[] { tmpURL });
        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            String output = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);


             //   HttpPost httppost = new HttpPost(url);



                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            outputText.setText(output);
        }
    }



//post
class AsyncT extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);

        try {

            JSONObject jsonobj = new JSONObject();

            jsonobj.put("firstname",msgTobeSent );

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));

            Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());

            // Use UrlEncodedFormEntity to send in proper format which we need
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            InputStream inputStream = response.getEntity().getContent();
            InputStreamToStringExample str = new InputStreamToStringExample();
              responseServer = str.getStringFromInputStream(inputStream);
            Log.e("response", "response -----" + responseServer);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        outputText.setText(responseServer);
    }
}

public static class InputStreamToStringExample {

    public static void main(String[] args) throws IOException {

        // intilize an InputStream
        InputStream is =
                new ByteArrayInputStream("file content..blah blah".getBytes());

        String result = getStringFromInputStream(is);

        System.out.println(result);
        System.out.println("Done");

    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
}

