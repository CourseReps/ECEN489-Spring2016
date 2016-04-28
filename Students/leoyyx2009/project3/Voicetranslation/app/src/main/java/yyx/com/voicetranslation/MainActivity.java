/**
 * @file MainActivity.java
 *
 * @brief This is the main activity for the APP
 *
 **/
package yyx.com.voicetranslation; // package name

//import
//----------------------------------------------------------------------------------------
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
//----------------------------------------------------------------------------------------

/**
 *
 * Author: Yanxiang Yang
 */

public class MainActivity extends ActionBarActivity {

    //public final static String EXTRA_MESSAGE = "yyx.com.voicetranslation.message";
    GoogleTranslateMainActivity translator;
    EditText translateedittext;
    TextView translatabletext;
    ImageButton speakbutton;
    ImageButton listenbutton;
    TextToSpeech textToSpeech;
    String targLan=null;
    String sourLan=null;
    Button changeButton;
    private ServerSocket serverSocket;

    Handler updateConversationHandler;

    Thread serverThread = null;
    
    public static final int SERVERPORT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.targetlanguages).
                setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView targetLanguages = (TextView) findViewById(R.id.tarLanOpion);
                        if (which == 0) {
                            targLan = "fr";
                            targetLanguages.setText("French");
                            textToSpeech.setLanguage(Locale.FRANCE);
                        } else if (which == 1) {
                            targLan = "zh";
                            targetLanguages.setText("Chinese");
                            textToSpeech.setLanguage(Locale.CHINA);
                        } else if (which == 2) {
                            targLan = "es";
                            targetLanguages.setText("Spanish");
                            textToSpeech.setLanguage(Locale.ENGLISH);
                        } else if (which == 3) {
                            targLan = "ja";
                            targetLanguages.setText("Japanese");
                            textToSpeech.setLanguage(Locale.JAPAN);
                        } else if (which == 4) {
                            targLan = "en";
                            targetLanguages.setText("English");
                            textToSpeech.setLanguage(Locale.ENGLISH);
                        }
                    }
                });



        changeButton = (Button) findViewById(R.id.LanOpionButton);
        changeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
        alertDialogBuilder1.setTitle(R.string.sourcelanguages).
                setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView sourceLanguages = (TextView) findViewById(R.id.sourLanOpion);
                        if (which == 0) {
                            sourLan = "fr";
                            sourceLanguages.setText("French");
                        } else if (which == 1) {
                            sourLan = "zh";
                            sourceLanguages.setText("Chinese");
                        } else if (which == 2) {
                            sourLan = "es";
                            sourceLanguages.setText("Spanish");
                        } else if (which == 3) {
                            sourLan = "ja";
                            sourceLanguages.setText("Japanese");
                        }else if (which == 4) {
                            sourLan = "en";
                            sourceLanguages.setText("English");
                        }
                    }
                });



        changeButton = (Button) findViewById(R.id.LanOpionButton1);
        changeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = alertDialogBuilder1.create();
                alertDialog.show();
            }
        });



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

        Button sendbutton = (Button) findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    public void run(){
                        sendMessage();
                    }
                }).start();
            }
        });


        updateConversationHandler = new Handler();

        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();



        speakbutton = (ImageButton) findViewById(R.id.SpeakBtn);
        speakbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    public void run(){
                        promptSpeechInput();
                    }

                }).start();

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
                    String address = "10.202.108.54";
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

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
     //   intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      //          RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
      //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.CHINA);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_CN");
      //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "say something ...");

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
            translated(sourLan, targLan);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public void translated(String sour, String lan){

        String translatetotagalog = translateedittext.getText().toString();//get the value of text
        String text = translator.translte(translatetotagalog, sour , lan);
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
            System.err.println(e);
        }

        }

    public void sendMessage(){
        Intent intent = new Intent(this, Client.class);
        startActivity(intent);

    }

    class ServerThread implements Runnable {



        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT,100);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    String read = input.readLine();

                    updateConversationHandler.post(new updateUIThread(read));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            EditText text = (EditText)findViewById(R.id.translateedittext);
            text.setText(msg + "\n");
        }
    }

}



