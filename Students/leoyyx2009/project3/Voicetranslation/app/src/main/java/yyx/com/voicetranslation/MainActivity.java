package yyx.com.voicetranslation;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * Yanxiang Yang
 */

public class MainActivity extends ActionBarActivity {
    GoogleTranslateMainActivity translator;
    EditText translateedittext;
    TextView translatabletext;
    ImageButton speakbutton;
    ImageButton listenbutton;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        translateedittext = (EditText) findViewById(R.id.translateedittext);
        Button translatebutton = (Button) findViewById(R.id.translatebutton);

        translatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new EnglishToTagalog().execute();

                try {

                } catch (Exception e) {
                    System.err.println(e); //no idea where this goes
                }

                }

        });






        speakbutton = (ImageButton) findViewById(R.id.SpeakBtn);
        speakbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRENCH);
                }
            }
        });

        listenbutton = (ImageButton) findViewById(R.id.listenBtn);
        listenbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            speakOut();
            }
        });



    }
    protected void sendHTTPdata(JSONObject json) throws Exception {
        final String data = json.toString();
        Thread t = new Thread() {
            public void run() {
                try {
                    //EditText address = (EditText) findViewById(R.id.remoteAddress);
                    //address.getText().toString()
                    String address = "10.202.108.57";
                    String serverAddress = "http://"+ address + ":8080/optout4/yy";
                    URL url = new URL(serverAddress);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(1000);
                    conn.setConnectTimeout(1000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("POST");
                    conn.connect();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                    writer.write(data);
                    writer.close();
                    os.close();

                    int result = conn.getResponseCode();
                    System.out.println(result);
                }catch(Exception e){
                    System.err.print(e);
                }
                System.out.println("Thread complete");
            }
        };
        t.start();
        System.out.println("thread started");
    }

    private void speakOut(){
        translatabletext = (TextView) findViewById(R.id.translatabletext);
        String toSpeak = translatabletext.getText().toString();
        textToSpeech.speak(toSpeak, textToSpeech.QUEUE_FLUSH, null, null);

    }

    public void onDestroy(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent,1000);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        translateedittext = (EditText) findViewById(R.id.translateedittext);

        switch (requestCode) {
            case 1000: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    translateedittext.setText(result.get(0));
                }
                break;
            }

        }
    }


    private class EnglishToTagalog extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        protected void onError(Exception ex) {

        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                translator = new GoogleTranslateMainActivity("AIzaSyC6GM5uq-KTaKohv_PazvgQ-GWPiT-6afk");

                Thread.sleep(1000);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(MainActivity.this, null, "Translating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();

        }
        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            super.onPostExecute(result);
            translated();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    public void translated(){

        String translatetotagalog = translateedittext.getText().toString();//get the value of text
        String text = translator.translte(translatetotagalog, "en", "fr");
        translatabletext = (TextView) findViewById(R.id.translatabletext);
        translatabletext.setText(text);

        JSONObject data = new JSONObject();
        try {
            EditText editText = (EditText) findViewById(R.id.translateedittext);
            String sourceWord = editText.getText().toString();
            TextView textView = (TextView) findViewById(R.id.translatabletext);
            String targetWord = textView.getText().toString();
            data.put("source", sourceWord);
            data.put("target", targetWord);

            sendHTTPdata(data);


        } catch (Exception e) {
            System.err.println(e); //no idea where this goes
        }

        }

}
