package com.example.akash.fskecen689;

import android.media.AudioRecord;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.example.akash.fskecen689.FSKConfig;
import com.example.akash.fskecen689.FSKDecoder;
import com.example.akash.fskecen689.FSKDecoder.FSKDecoderCallback;
import com.example.akash.fskecen689.FSKEncoder;
import com.example.akash.fskecen689.FSKEncoder.FSKEncoderCallback;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {
    public static  String ENCODER_DATA = "Hello world";

    EditText fskText;
    TextView txview2;

    Button encoderButton;

    protected FSKConfig mConfig;
    protected FSKEncoder mEncoder;
    protected FSKDecoder mDecoder;

    protected AudioTrack mAudioTrack;

    protected Runnable mPCMFeeder = new Runnable() {

        @Override
        public void run() {
            try {
                //open input stream to the WAV file
                InputStream input = getResources().getAssets().open("download.wav");

                //get information about the WAV file
                WavToPCM.WavInfo info = WavToPCM.readHeader(input);

                //get the raw PCM data
                ByteBuffer pcm = ByteBuffer.wrap(WavToPCM.readWavPcm(info, input));

                //the decoder has 1 second buffer (equals to sample rate),
                //so we have to fragment the entire file,
                //to prevent buffer overflow or rejection
                byte[] buffer = new byte[1024];

                //feed signal little by little... another way to do that is to
                //check the returning value of appendSignal(), it returns the
                //remaining space in the decoder signal buffer
                //fsk decoder need to recorder the
                while (pcm.hasRemaining()) {

                    if (pcm.remaining() > 1024) {
                        pcm.get(buffer);
                    }
                    else {
                        buffer = new byte[pcm.remaining()];

                        pcm.get(buffer);
                    }

                    mDecoder.appendSignal(buffer);

                    Thread.sleep(100);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // encoding

    protected Runnable mDataFeeder = new Runnable() {

        @Override
        public void run() {
            fskText = (EditText) findViewById(R.id.editText);
            ENCODER_DATA = fskText.getText().toString();

            byte[] data = ENCODER_DATA.getBytes();

            if (data.length > FSKConfig.ENCODER_DATA_BUFFER_SIZE) {
                //chunk data

                byte[] buffer = new byte[FSKConfig.ENCODER_DATA_BUFFER_SIZE];

                ByteBuffer dataFeed = ByteBuffer.wrap(data);

                while (dataFeed.remaining() > 0) {

                    if (dataFeed.remaining() < buffer.length) {
                        buffer = new byte[dataFeed.remaining()];
                    }

                    dataFeed.get(buffer);

                    mEncoder.appendData(buffer);

                    try {
                        Thread.sleep(100); //wait for encoder to do its job, to avoid buffer overflow and data rejection
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                mEncoder.appendData(data);
            }
        }
    };

    public void fskClick(View view){

        try {
            mConfig = new FSKConfig(FSKConfig.SAMPLE_RATE_44100, FSKConfig.PCM_8BIT, FSKConfig.CHANNELS_MONO, FSKConfig.SOFT_MODEM_MODE_1, FSKConfig.THRESHOLD_20P);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /// INIT FSK DECODER

        mDecoder = new FSKDecoder(mConfig, new FSKDecoderCallback() {

            @Override
            public void decoded(byte[] newData) {

                final String text = new String(newData);

                runOnUiThread(new Runnable() {
                    public void run() {

                        TextView txview = ((TextView) findViewById(R.id.result));
                        String tmpText = fskText.getText().toString();
                        txview.setText(tmpText);
                    }
                });
            }
        });

        /// INIT FSK ENCODER

        mEncoder = new FSKEncoder(mConfig, new FSKEncoderCallback() {

            @Override
            public void encoded(byte[] pcm8, short[] pcm16) {
                if (mConfig.pcmFormat == FSKConfig.PCM_8BIT) {
                    //8bit buffer is populated, 16bit buffer is null

                    mAudioTrack.write(pcm8, 0, pcm8.length);

                    mDecoder.appendSignal(pcm8);
                }
                else if (mConfig.pcmFormat == FSKConfig.PCM_16BIT) {
                    //16bit buffer is populated, 8bit buffer is null

                    mAudioTrack.write(pcm16, 0, pcm16.length);

                    mDecoder.appendSignal(pcm16);
                }
            }
        });

        ///

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                mConfig.sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, 1024,
                AudioTrack.MODE_STREAM);
        mAudioTrack.play();

        //akash
        // Prepare the AudioRecord & AudioTrack

        //

        ///

        new Thread(mDataFeeder).start();


    }

    public void fskClickDecode(View view) {
        try {
            mConfig = new FSKConfig(FSKConfig.SAMPLE_RATE_29400, FSKConfig.PCM_8BIT, FSKConfig.CHANNELS_MONO, FSKConfig.SOFT_MODEM_MODE_4, FSKConfig.THRESHOLD_20P);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //txview2.setText("# ");
        /// INIT FSK DECODER

        mDecoder = new FSKDecoder(mConfig, new FSKDecoderCallback() {

            @Override
            public void decoded(byte[] newData) {

                final String text = new String(newData);

                runOnUiThread(new Runnable() {
                    public void run() {

                     //   TextView view = ((TextView) findViewById(R.id.result));

                        TextView  txview2 = ((TextView) findViewById(R.id.decodedText));
                         txview2.setText(txview2.getText()+text);
                        //view.setText(view.getText()+text);
                    }
                });
            }
        });

        ///

        new Thread(mPCMFeeder).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//



    }
    @Override
    protected void onDestroy() {
        mDecoder.stop();

        mEncoder.stop();

        mAudioTrack.stop();
        mAudioTrack.release();

        super.onDestroy();
    }

}

