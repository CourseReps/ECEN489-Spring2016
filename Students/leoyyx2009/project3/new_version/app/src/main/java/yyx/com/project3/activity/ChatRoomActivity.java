/**
 * @file ChatRoomActivity.java
 *
 * Yanxiang Yang
 *
 **/
package yyx.com.project3.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import yyx.com.project3.R;
import yyx.com.project3.activity.*;
import yyx.com.project3.activity.GoogleTranslateMainActivity;
import yyx.com.project3.adapter.ChatRoomThreadAdapter;
import yyx.com.project3.app.Config;
import yyx.com.project3.app.EndPoints;
import yyx.com.project3.app.MyApplication;
import yyx.com.project3.gcm.NotificationUtils;
import yyx.com.project3.model.Message;
import yyx.com.project3.model.User;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class ChatRoomActivity extends AppCompatActivity {

    private String TAG = yyx.com.project3.activity.ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    private Button translatebutton;
    //---
    GoogleTranslateMainActivity translator;
    TextView translatabletext;
    ImageButton speakbutton;
    ImageButton listenbutton;
    TextToSpeech textToSpeech;
    String targLan = "zh";
    String sourLan = "en";
    Button changeButton;
    private String key;
    private String message_translate;

    //----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //----
        translatabletext = (TextView) findViewById(R.id.translatabletext);


        translatebutton = (Button) findViewById(R.id.translatebutton);
        translatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //
               // final String text = ((EditText) findViewById(R.id.message)).getText().toString();

                class bgStuff extends AsyncTask<Void, Void, Void>{

                    String translatedText = "";
                    @Override
                    protected Void doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        try {

                            String text= message_translate;
                            translatedText = translate(text);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            translatedText = e.toString();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        // TODO Auto-generated method stub
                        ((TextView) findViewById(R.id.translatabletext)).setText(translatedText);
                        super.onPostExecute(result);
                    }

                }

                new bgStuff().execute();
            }



        });

        speakbutton = (ImageButton) findViewById(R.id.SpeakBtn);
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

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
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

        //-----

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btn_send);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        String title = "ECEN489-689";
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });

        fetchChatThread();
    }


    /**
     * @fn speakOut
     * @brief use textToSpeech API
     */
    private void speakOut() {
        String toSpeak = translatabletext.getText().toString();
        textToSpeech.speak(toSpeak, textToSpeech.QUEUE_FLUSH, null, null);

    }
    public String translate(String text) throws Exception{


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("leoyyx2009"); //Change this
        Translate.setClientSecret("a6OqAafzSYis4ZKMWd+EWgE4g5NvwhGnJ9faDcEhCyM="); //change


        String translatedText = "";

        translatedText = Translate.execute(text, Language.ENGLISH);

        return translatedText;
    }

    /**
     * @fn onDestroy
     * @brief stop socket server and textToSpeech
     */
    public void onDestroy() {

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    /**
     * @fn promptSpeechInput
     * @brief open the recognizer
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh_CN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "say something ...");
        try {
            startActivityForResult(intent, 1000);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    //----


    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);

            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
            //---
            message_translate = message.getMessage();
            //---
        }
    }


    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    private void sendMessage() {
        final String message = this.inputMessage.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

        Log.e(TAG, "endpoint: " + endPoint);

        this.inputMessage.setText("");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONObject commentObj = obj.getJSONObject("message");

                        String commentId = commentObj.getString("message_id");
                        String commentText = commentObj.getString("message");
                        String createdAt = commentObj.getString("created_at");

                        JSONObject userObj = obj.getJSONObject("user");
                        String userId = userObj.getString("user_id");
                        String userName = userObj.getString("name");
                        User user = new User(userId, userName, null);

                        Message message = new Message();
                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setCreatedAt(createdAt);
                        message.setUser(user);

                        messageArrayList.add(message);

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                inputMessage.setText(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                params.put("message", message);

                Log.e(TAG, "Params: " + params.toString());

                return params;
            }

            ;
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
//---


    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchChatThread() {

        String endPoint = EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String commentId = commentObj.getString("message_id");
                            String commentText = commentObj.getString("message");
                            String createdAt = commentObj.getString("created_at");

                            JSONObject userObj = commentObj.getJSONObject("user");
                            String userId = userObj.getString("user_id");
                            String userName = userObj.getString("username");
                            User user = new User(userId, userName, null);

                            Message message = new Message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            message.setUser(user);

                            messageArrayList.add(message);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    /**
     * @fn onActivityResult
     * @brief Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 1000: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputMessage.setText(result.get(0));
                }
                break;
            }

        }


    }


}

