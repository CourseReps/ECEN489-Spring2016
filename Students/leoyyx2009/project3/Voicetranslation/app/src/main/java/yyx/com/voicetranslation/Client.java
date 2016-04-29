/**
 * @file Client.java
 *
 * Yanxiang Yang
 *
 * @brief This is an activity to run as a client so as to communicate with server
 *
 **/
package yyx.com.voicetranslation;

//import
//----------------------------------------------------------------------------------------
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
//----------------------------------------------------------------------------------------

/**
 * @class Client
 *
 * @brief use socket outputstream to send data to server
 *
 */
public class Client extends Activity {

    private Socket socket;

    private static final int SERVERPORT = 6000;
    private String SERVER_IP = "172.17.106.250";//"172.17.106.250";

    private ImageButton speakbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_client);

        /*
        Button enter=(Button)findViewById(R.id.myButton1);
        enter.setOnClickListener(new View.OnClickListener() {
            EditText et1 = (EditText) findViewById(R.id.EditText02);

            @Override
            public void onClick(View v) {
                SERVER_IP = et1.getText().toString();
            }

        });
        */
        speakbutton = (ImageButton) findViewById(R.id.SpeakBtn1);
        speakbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        promptSpeechInput();
                    }
                }).start();
            }
        });

        new Thread(new ClientThread()).start();

    }

    public void onClick(View view) {
            try {
                EditText et = (EditText) findViewById(R.id.EditText01);
                String str = et.getText().toString();

                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println(str);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * @fn promptSpeechInput
     * @brief open the recognizer
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_CN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 1000);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @fn onActivityResult
     * @brief Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EditText editText = (EditText) findViewById(R.id.EditText01);

        switch (requestCode) {
            case 1000: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                }
                break;
            }

        }
    }

    /**
     * @class ClientTread
     *
     * @brief initialize the client, and establish the connection
     *
     */
    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
}